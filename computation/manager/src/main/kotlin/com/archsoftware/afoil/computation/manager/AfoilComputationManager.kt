package com.archsoftware.afoil.computation.manager

import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import com.archsoftware.afoil.computation.manager.model.asEngineModel
import com.archsoftware.afoil.computation.manager.model.asExternalModel
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import com.archsoftware.afoil.core.common.datairfoilreader.AirfoilReader
import com.archsoftware.afoil.core.common.datairfoilreader.Coordinate
import com.archsoftware.afoil.core.data.repository.ProjectDataRepository
import com.archsoftware.afoil.core.data.repository.ProjectNumResultRepository
import com.archsoftware.afoil.core.data.repository.ProjectRepository
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AfoilProjectNumResult
import com.archsoftware.afoil.core.model.AirfoilAnalysisNumResult
import com.archsoftware.afoil.core.model.AirfoilAnalysisProjectData
import com.archsoftware.afoil.core.model.ComputationLog
import com.archsoftware.afoil.core.projectstore.ProjectStore
import com.archsoftware.afoil.engine.InviscidSolver
import com.archsoftware.afoil.engine.PanelsGenerator
import com.archsoftware.afoil.engine.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class AfoilComputationManager @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val projectDataRepository: ProjectDataRepository,
    private val projectNumResultRepository: ProjectNumResultRepository,
    private val projectStore: ProjectStore,
    private val datAirfoilReader: AirfoilReader,
    private val contentResolver: UriContentResolver,
    @Dispatcher(AfoilDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher
) : ComputationManager {
    private val _computationState: MutableSharedFlow<ComputationManager.State> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val _logs: MutableSharedFlow<List<ComputationLog>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val _progress: MutableSharedFlow<Float> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    @VisibleForTesting
    internal lateinit var computationJob: Job
    private lateinit var computationScope: CoroutineScope

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _computationState.tryEmit(ComputationManager.State.ERROR)
    }

    override fun getComputationState(): Flow<ComputationManager.State> = _computationState

    override fun getComputationLogs(): Flow<List<ComputationLog>> = _logs

    override fun getComputationProgress(): Flow<Float> = _progress

    override fun startComputation(projectId: Long?) {
        startComputation(projectId) { id ->
            val project = projectRepository.getProjectById(id).first()
            val projectDataFileUri = projectDataRepository.getProjectDataByProjectId(id).first().uri
            when (val projectData = projectStore.readProjectData(projectDataFileUri.toUri())) {
                is AirfoilAnalysisProjectData -> {
                    airfoilAnalysisComputation(project, projectData)
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    override fun startComputation(projectId: Long?, computation: suspend CoroutineScope.(id: Long) -> Unit) {
        computationJob = Job()
        computationScope = CoroutineScope(computationJob + defaultDispatcher)
        computationScope.launch(exceptionHandler) {
            if (projectId == null || projectId < 0) throw IllegalArgumentException()

            _computationState.emit(ComputationManager.State.RUNNING)

            computation(projectId)

            _computationState.emit(ComputationManager.State.FINISHED)
        }
    }

    override fun stopComputation(canceled: Boolean) {
        computationScope.launch {
            if (canceled) {
                _computationState.emit(ComputationManager.State.CANCELED)
            }
            computationJob.cancel()
            clear()
        }
    }

    private fun clear() {
        // Clear all data since ComputationManager is a singleton
        _computationState.resetReplayCache()
        _logs.resetReplayCache()
        _progress.resetReplayCache()
    }

    /**
     * Setups and launches an airfoil analysis computation with the provided data.
     */
    private suspend fun CoroutineScope.airfoilAnalysisComputation(
        project: AfoilProject,
        data: AirfoilAnalysisProjectData
    ) {
        // TODO: Use generic solver which will choose internally to use inviscid or viscous procedure
        val dirUri = project.dirUri.toUri()
        val rawPoints = datAirfoilReader.readCoordinates(
            contentResolver.findDocument(dirUri, data.datAirfoilDisplayName)
        )
        if (rawPoints.isEmpty()) return
        val logger = Logger()
        val logs = mutableListOf<ComputationLog>()
        logger.log.onEach {
            logs += it.asExternalModel()
            _logs.emit(logs)
        }.launchIn(this)
        val panelsGenerator = PanelsGenerator(data.panelsNumber, logger)
        val points = panelsGenerator.generate(rawPoints.map(Coordinate::asEngineModel)) ?: return
        val inviscidSolver = InviscidSolver(points, data.machNumber, data.angleOfAttack, logger)
        val inviscidSolution = inviscidSolver.solve() ?: return
        val numResult = AirfoilAnalysisNumResult(
            gammas = inviscidSolution.gammas.toList(),
            psi0 = inviscidSolution.psi0,
            cl = inviscidSolver.cl(inviscidSolution.gammas),
            cd = 0.0, // TODO: Add viscous solver to afoil-engine library
            cm = inviscidSolver.cm(inviscidSolution.gammas)
        )
        val numResultFileUri = projectStore.writeProjectNumResult(dirUri, numResult) ?: return
        val afoilProjectNumResult = AfoilProjectNumResult(
            uri = numResultFileUri.toString(),
            projectOwnerId = project.id
        )
        projectNumResultRepository.insertProjectNumResult(afoilProjectNumResult)
    }
}