package com.archsoftware.afoil.computation.manager

import androidx.annotation.VisibleForTesting
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.data.repository.ProjectRepository
import com.archsoftware.afoil.core.model.ComputationLog
import com.archsoftware.afoil.core.projectstore.ProjectStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ComputationManager @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val projectStore: ProjectStore,
    @Dispatcher(AfoilDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher
) {
    private val _computationState: MutableSharedFlow<State> = MutableSharedFlow(replay = 1)
    val computationState: Flow<State> = _computationState

    private val _logs: MutableSharedFlow<List<ComputationLog>> = MutableSharedFlow(replay = 1)
    val logs: Flow<List<ComputationLog>> = _logs

    private val _progress: MutableSharedFlow<Float> = MutableSharedFlow(replay = 1)
    val progress: Flow<Float> = _progress

    @VisibleForTesting
    internal val computationJob: Job = Job()
    private val computationScope: CoroutineScope = CoroutineScope(computationJob + defaultDispatcher)

    fun startComputation(projectName: String?) {
        computationScope.launch {
            if (projectName == null) {
                _computationState.emit(State.ERROR)
                return@launch
            }

            _computationState.emit(State.RUNNING)

            // TODO: Retrieve project from repository and pass projectDataType to project
            //  store and implement getUri(projectName) method in ProjectStore

            _computationState.emit(State.FINISHED)
        }
    }

    fun stopComputation() {
        if (computationJob.isActive) {
            _computationState.tryEmit(State.CANCELED)
            computationJob.cancel()
        }
    }

    enum class State {
        RUNNING,
        ERROR,
        FINISHED,
        CANCELED
    }
}