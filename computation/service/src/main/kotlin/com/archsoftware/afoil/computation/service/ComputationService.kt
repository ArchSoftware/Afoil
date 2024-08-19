package com.archsoftware.afoil.computation.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.core.common.AfoilDispatcher
import com.archsoftware.afoil.core.common.Dispatcher
import com.archsoftware.afoil.core.notifications.Notifier
import com.archsoftware.afoil.core.notifications.SystemTrayNotifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

const val EXTRA_PROJECT_NAME = "com.archsoftware.afoil.intent.extra.PROJECT_NAME"

@AndroidEntryPoint
class ComputationService : Service() {

    @Inject
    lateinit var notifier: Notifier

    @Inject
    lateinit var computationManager: ComputationManager

    @Inject
    @Dispatcher(AfoilDispatcher.Default)
    lateinit var defaultDispatcher: CoroutineDispatcher

    @VisibleForTesting
    internal val collectJob: Job = Job()
    private val serviceScope: CoroutineScope by lazy {
        CoroutineScope(collectJob + defaultDispatcher)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val projectName = intent?.getStringExtra(EXTRA_PROJECT_NAME)
        // Call startForeground() as soon as possible to avoid exceptions
        startForeground(projectName)
        computationManager.startComputation(projectName)

        serviceScope.launch {
            computationManager.getComputationProgress().collect { progress ->
                notifier.updateComputationServiceNotification(progress)
            }
        }

        serviceScope.launch {
            computationManager.getComputationState().collect { state ->
                if (state != ComputationManager.State.RUNNING) {
                    stopSelf()
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        collectJob.cancel()
        computationManager.stopComputation()
    }

    private fun startForeground(computationName: String?) {
        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ SystemTrayNotifier.COMPUTATION_SERVICE_NOTIFICATION_ID,
            /* notification = */ notifier.createComputationServiceNotification(computationName),
            /* foregroundServiceType = */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                0
            }
        )
    }

    companion object {
        fun createStartIntent(
            context: Context,
            projectName: String
        ): Intent = Intent(context, ComputationService::class.java).apply {
            putExtra(EXTRA_PROJECT_NAME, projectName)
        }

        fun createStopIntent(context: Context): Intent =
            Intent(context, ComputationService::class.java)
    }
}