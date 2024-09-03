package com.archsoftware.afoil.computation.manager

import com.archsoftware.afoil.core.model.ComputationLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ComputationManager {
    fun getComputationState(): Flow<State>
    fun getComputationLogs(): Flow<List<ComputationLog>>
    fun getComputationProgress(): Flow<Float>

    fun startComputation(projectId: Long?)
    fun startComputation(projectId: Long?, computation: suspend CoroutineScope.(id: Long) -> Unit)
    fun stopComputation(canceled: Boolean)

    enum class State {
        IDLE,
        RUNNING,
        ERROR,
        FINISHED,
        CANCELED
    }
}