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

const val EXTRA_PROJECT_ID = "com.archsoftware.afoil.intent.extra.PROJECT_ID"
const val EXTRA_PROJECT_NAME = "com.archsoftware.afoil.intent.extra.PROJECT_NAME"

/**
 * Foreground [Service] used to host the computation.
 *
 * It serves as communication interface between clients and the [ComputationManager]. It manages
 * notification progress updates and automatically stop the computation whenever it's finished or
 * an error occurs.
 */
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

    // Whether the service was canceled by the user or the system
    private var canceled: Boolean = true

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val projectName = intent?.getStringExtra(EXTRA_PROJECT_NAME)
        val projectId = intent?.getLongExtra(EXTRA_PROJECT_ID, -1)
        // Call startForeground() as soon as possible to avoid exceptions
        startForeground(projectId, projectName)
        computationManager.startComputation(projectId)

        serviceScope.launch {
            computationManager.getComputationProgress().collect { progress ->
                // Computation manager progress is between 0 and 1, convert it to a percentage
                notifier.updateComputationServiceNotification((progress * 100).toInt())
            }
        }

        serviceScope.launch {
            computationManager.getComputationState().collect { state ->
                if (state != ComputationManager.State.RUNNING) {
                    canceled = false
                    stopSelf()
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        collectJob.cancel()
        computationManager.stopComputation(canceled)
    }

    private fun startForeground(projectId: Long?, computationName: String?) {
        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ SystemTrayNotifier.COMPUTATION_SERVICE_NOTIFICATION_ID,
            /* notification = */ notifier.createComputationServiceNotification(projectId, computationName),
            /* foregroundServiceType = */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                0
            }
        )
    }

    companion object {
        /**
         * Creates an intent to start the [ComputationService].
         *
         * @param context A Context of the application package implementing this class.
         * @param projectId The ID of the project to start the computation for.
         * @param projectName The name of the project to start the computation for.
         * @return An intent to start the [ComputationService].
         */
        fun createStartIntent(
            context: Context,
            projectId: Long,
            projectName: String
        ): Intent = Intent(context, ComputationService::class.java).apply {
            putExtra(EXTRA_PROJECT_ID, projectId)
            putExtra(EXTRA_PROJECT_NAME, projectName)
        }

        /**
         * Creates an intent to stop the [ComputationService].
         *
         * @param context A Context of the application package implementing this class.
         * @return An intent to stop the [ComputationService].
         */
        fun createStopIntent(context: Context): Intent =
            Intent(context, ComputationService::class.java)
    }
}