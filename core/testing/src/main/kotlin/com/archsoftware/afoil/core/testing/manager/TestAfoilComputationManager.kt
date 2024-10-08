package com.archsoftware.afoil.core.testing.manager

import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.core.model.ComputationLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.annotations.TestOnly

/**
 * Test-only implementation of the [ComputationManager] interface.
 */
@TestOnly
class TestAfoilComputationManager : ComputationManager {
    private val _computationState: MutableSharedFlow<ComputationManager.State> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getComputationState(): Flow<ComputationManager.State> = _computationState
    override fun getComputationLogs(): Flow<List<ComputationLog>> = emptyFlow()
    override fun getComputationProgress(): Flow<Float> = emptyFlow()

    override fun startComputation(projectId: Long?) {
        _computationState.tryEmit(ComputationManager.State.RUNNING)
    }

    override fun startComputation(projectId: Long?, computation: suspend CoroutineScope.(id: Long) -> Unit) {}

    override fun stopComputation(canceled: Boolean) {
        if (canceled) _computationState.tryEmit(ComputationManager.State.CANCELED)
    }

    fun sendState(state: ComputationManager.State) {
        _computationState.tryEmit(state)
    }
}