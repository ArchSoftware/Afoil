package com.archsoftware.afoil.computation.service.connection

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.computation.service.ComputationService
import com.archsoftware.afoil.core.model.ComputationLog
import kotlinx.coroutines.flow.Flow

class ComputationConnection : ServiceConnection {
    private lateinit var computationService: ComputationService

    val state: Flow<ComputationManager.State> by lazy { computationService.state }
    val logs: Flow<List<ComputationLog>> by lazy { computationService.logs }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as ComputationService.ComputationBinder
        computationService = binder.getService()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
    }
}