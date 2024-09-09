package com.archsoftware.afoil.computation.manager

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import com.archsoftware.afoil.computation.manager.model.asEngineModel
import com.archsoftware.afoil.computation.manager.model.asExternalModel
import com.archsoftware.afoil.computation.manager.utils.generatePressurePlotBitmap
import com.archsoftware.afoil.computation.manager.utils.generateStreamlinesPlotBitmap
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.common.R
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import com.archsoftware.afoil.core.common.datairfoilreader.AirfoilReader
import com.archsoftware.afoil.core.common.datairfoilreader.Coordinate
import com.archsoftware.afoil.core.common.utils.use
import com.archsoftware.afoil.core.data.repository.ProjectDataRepository
import com.archsoftware.afoil.core.data.repository.ProjectNumResultRepository
import com.archsoftware.afoil.core.data.repository.ProjectPostResultRepository
import com.archsoftware.afoil.core.data.repository.ProjectRepository
import com.archsoftware.afoil.core.math.utils.toRad
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.AfoilProjectNumResult
import com.archsoftware.afoil.core.model.AfoilProjectPostResult
import com.archsoftware.afoil.core.model.AirfoilAnalysisNumResult
import com.archsoftware.afoil.core.model.AirfoilAnalysisProjectData
import com.archsoftware.afoil.core.model.ComputationLog
import com.archsoftware.afoil.core.model.PressurePlotResult
import com.archsoftware.afoil.core.model.StreamlinesPlotResult
import com.archsoftware.afoil.core.projectstore.ProjectStore
import com.archsoftware.afoil.engine.InviscidSolver
import com.archsoftware.afoil.engine.PanelsGenerator
import com.archsoftware.afoil.engine.logger.Logger
import com.archsoftware.afoil.engine.model.ComputationalField
import com.archsoftware.afoil.engine.postprocessing.PressurePlot
import com.archsoftware.afoil.engine.postprocessing.StreamlinesPlot
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

private const val MIN_STREAMLINES_NUMBER = 1
private const val MIN_PRESSURE_CONTOURS_GRID_SIZE = 2
private const val MIN_STREAMLINES_DX_STEP = 1e-2 // 1/100 of the chord length
private const val MAX_STREAMLINES_DX_STEP = 1e-1 // 1/10 of the chord length

/**
 * Singleton implementation of the [ComputationManager] interface.
 *
 * Manages the computation lifecycle through the [startComputation] and [stopComputation] methods.
 * It also notifies clients of computation state and progress.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class AfoilComputationManager @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val projectDataRepository: ProjectDataRepository,
    private val projectNumResultRepository: ProjectNumResultRepository,
    private val projectPostResultRepository: ProjectPostResultRepository,
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

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("ComputationManager", "Computation failed", throwable)
        _computationState.tryEmit(ComputationManager.State.ERROR)
    }

    override fun getComputationState(): Flow<ComputationManager.State> = _computationState

    override fun getComputationLogs(): Flow<List<ComputationLog>> = _logs

    override fun getComputationProgress(): Flow<Float> = _progress

    /**
     * Starts the computation for the given project.
     *
     * @param projectId The ID of the project to start the computation for.
     */
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

    /**
     * Executes the given computation block under a new computation job and scope.
     *
     * @param projectId The project ID to pass to the computation block.
     * @param computation The computation block to execute.
     */
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

    /**
     * Stops the computation.
     *
     * @param canceled Whether the computation was canceled.
     */
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
        // Read project data
        val dirUri = project.dirUri.toUri()
        val rawPoints = datAirfoilReader.readCoordinates(
            contentResolver.findDocument(dirUri, data.datAirfoilDisplayName)
        )
        if (rawPoints.isEmpty()) return

        // Setup logger
        val logger = Logger(
            processIds = buildList {
                add(PanelsGenerator.PANELS_GENERATOR_TAG)
                add(InviscidSolver.INVISCID_SOLVER_TAG)
                add(PressurePlot.PRESSURE_PLOT_TAG)
                if (data.numberOfStreamlines >= MIN_STREAMLINES_NUMBER) {
                    add(StreamlinesPlot.STREAMLINES_PLOT_TAG)
                }
                if (data.pressureContoursGridSize >= MIN_PRESSURE_CONTOURS_GRID_SIZE) {
                    // TODO: Add pressure contours plot
                }
            }
        )
        val logs = mutableListOf<ComputationLog>()
        logger.progress.onEach {
            _progress.emit(it)
        }.launchIn(this)
        logger.log.onEach {
            logs += it.asExternalModel()
            _logs.emit(logs)
        }.launchIn(this)

        // Setup computation
        val panelsGenerator = PanelsGenerator(data.panelsNumber, logger)
        val points = panelsGenerator.generate(rawPoints.map(Coordinate::asEngineModel)) ?: return
        val inviscidSolver = InviscidSolver(points, data.machNumber, data.angleOfAttack.toRad(), logger)
        val inviscidSolution = inviscidSolver.solve() ?: return

        // Store numerical results
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

        // Generate pressure distribution plot
        val pressurePlot = PressurePlot(
            airfoilPoints = points,
            inviscidSolver = inviscidSolver,
            inviscidSolution = inviscidSolution,
            logger = logger
        )
        val pressureDistribution = pressurePlot.generate() ?: return
        pressureDistribution.generatePressurePlotBitmap(points).use { bitmap ->
            val pressurePlotResult = PressurePlotResult(bitmap)
            // Store pressure distribution plot result
            val pressurePlotFileUri = projectStore.writeProjectPostResult(
                dirUri = dirUri,
                result = pressurePlotResult
            ) ?: return
            val afoilProjectPressurePlotResult = AfoilProjectPostResult(
                nameId = R.string.pressure_plot,
                uri = pressurePlotFileUri.toString(),
                projectOwnerId = project.id
            )
            projectPostResultRepository.insertProjectPostResult(afoilProjectPressurePlotResult)
        }

        if (data.numberOfStreamlines < MIN_STREAMLINES_NUMBER &&
            data.pressureContoursGridSize < MIN_PRESSURE_CONTOURS_GRID_SIZE) {
            return
        }

        // Use a square shaped computational field with x in [-0.5, 1.5] and y in [-1.0, 1.0]
        val computationalField = ComputationalField(-0.5, 1.0, 1.5, -1.0)

        if (data.numberOfStreamlines >= MIN_STREAMLINES_NUMBER) {
            // Linear variation of the dx step length with the refinement level
            val dx =
                MIN_STREAMLINES_DX_STEP +
                        (MAX_STREAMLINES_DX_STEP - MIN_STREAMLINES_DX_STEP) *
                        (1 - data.streamlinesRefinementLevel)
            val streamlinesPlot = StreamlinesPlot(
                dx = dx,
                computationalField = computationalField,
                inviscidSolver = inviscidSolver,
                inviscidSolution = inviscidSolution,
                logger = logger
            )
            val streamlines = streamlinesPlot.generate(data.numberOfStreamlines) ?: return
            streamlines.generateStreamlinesPlotBitmap(
                computationalField = computationalField,
                airfoilPoints = points
            ).use { bitmap ->
                val streamlinesPlotResult = StreamlinesPlotResult(bitmap)
                // Store streamlines plot result
                val streamlinesPlotFileUri = projectStore.writeProjectPostResult(
                    dirUri = dirUri,
                    result = streamlinesPlotResult
                ) ?: return
                val afoilProjectStreamlinesPlotResult = AfoilProjectPostResult(
                    nameId = R.string.streamlines_plot,
                    uri = streamlinesPlotFileUri.toString(),
                    projectOwnerId = project.id
                )
                projectPostResultRepository.insertProjectPostResult(afoilProjectStreamlinesPlotResult)
            }
        }

        if (data.pressureContoursGridSize >= MIN_PRESSURE_CONTOURS_GRID_SIZE) {
            // TODO: Add pressure contours plot
        }
    }
}