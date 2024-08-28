package com.archsoftware.afoil.computation.manager

import androidx.annotation.VisibleForTesting
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.data.repository.ProjectRepository
import com.archsoftware.afoil.core.model.ComputationLog
import com.archsoftware.afoil.core.projectstore.ProjectStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class AfoilComputationManager @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val projectStore: ProjectStore,
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
        startComputation(projectId) {
            // TODO: Retrieve project from repository and pass projectDataType to project
            //  store and implement getUri(projectName) method in ProjectStore
        }
    }

    override fun startComputation(projectId: Long?, computation: suspend () -> Unit) {
        computationJob = Job()
        computationScope = CoroutineScope(computationJob + defaultDispatcher)
        computationScope.launch(exceptionHandler) {
            if (projectId == null || projectId < 0) throw IllegalArgumentException()

            _computationState.emit(ComputationManager.State.RUNNING)

            computation()

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
}