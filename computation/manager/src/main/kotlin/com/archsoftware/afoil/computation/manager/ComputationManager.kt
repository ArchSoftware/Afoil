package com.archsoftware.afoil.computation.manager

import com.archsoftware.afoil.core.model.ComputationLog
import kotlinx.coroutines.flow.Flow

interface ComputationManager {
    fun getComputationState(): Flow<State>
    fun getComputationLogs(): Flow<List<ComputationLog>>
    fun getComputationProgress(): Flow<Float>

    fun startComputation(projectName: String?)
    fun stopComputation()

    enum class State {
        RUNNING,
        ERROR,
        FINISHED,
        CANCELED
    }
}