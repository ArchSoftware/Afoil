package com.archsoftware.afoil.computation.service.connection

import android.content.ComponentName
import android.os.IBinder
import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.computation.service.ComputationService
import com.archsoftware.afoil.core.model.ComputationLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComputationConnection @Inject constructor() : ComputationServiceConnection {
    private lateinit var computationService: ComputationService

    override val state: Flow<ComputationManager.State> by lazy { computationService.state }
    override val logs: Flow<List<ComputationLog>> by lazy { computationService.logs }
    override val progress: Flow<Float> by lazy { computationService.progress }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as ComputationService.ComputationBinder
        computationService = binder.getService()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
    }
}