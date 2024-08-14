package com.archsoftware.afoil.computation.service.connection

import android.content.ServiceConnection
import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.core.model.ComputationLog
import kotlinx.coroutines.flow.Flow

interface ComputationServiceConnection : ServiceConnection {
    val state: Flow<ComputationManager.State>
    val logs: Flow<List<ComputationLog>>
    val progress: Flow<Float>
}