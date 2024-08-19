package com.archsoftware.afoil.computation.manager

import com.archsoftware.afoil.core.model.ComputationLog
import kotlinx.coroutines.flow.Flow

interface ComputationManager {
    fun getComputationState(): Flow<State>
    fun getComputationLogs(): Flow<List<ComputationLog>>
    fun getComputationProgress(): Flow<Float>

    fun startComputation(projectName: String?)
    fun startComputation(projectName: String?, computation: suspend () -> Unit)
    fun stopComputation()

    enum class State {
        IDLE,
        RUNNING,
        ERROR,
        FINISHED,
        CANCELED
    }
}