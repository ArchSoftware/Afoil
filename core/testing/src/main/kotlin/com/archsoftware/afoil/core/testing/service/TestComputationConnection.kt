package com.archsoftware.afoil.core.testing.service

import android.content.ComponentName
import android.os.IBinder
import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.computation.service.connection.ComputationServiceConnection
import com.archsoftware.afoil.core.model.ComputationLog
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.annotations.TestOnly

@TestOnly
class TestComputationConnection : ComputationServiceConnection {
    private val stateFlow: MutableSharedFlow<ComputationManager.State> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val state: Flow<ComputationManager.State> = stateFlow
    override val logs: Flow<List<ComputationLog>> = emptyFlow()
    override val progress: Flow<Float> = emptyFlow()

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {}

    override fun onServiceDisconnected(name: ComponentName?) {}

    fun sendState(state: ComputationManager.State) {
        stateFlow.tryEmit(state)
    }
}