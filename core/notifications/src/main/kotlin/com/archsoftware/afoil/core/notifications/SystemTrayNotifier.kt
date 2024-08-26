package com.archsoftware.afoil.core.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val COMPUTATION_SERVICE_NOTIFICATION_CHANNEL_ID = "computation_service_channel"
private const val COMPUTATION_SERVICE_NOTIFICATION_REQUEST_CODE = 0
private const val TARGET_ACTIVITY_NAME = "com.archsoftware.afoil.MainActivity"
private const val DEEP_LINK_SCHEME_AND_HOST = "https://www.afoil.archsoftware.com"
private const val COMPUTATION_MONITOR = "computationmonitor"
private const val PROGRESS_MAX = 100

@Singleton
class SystemTrayNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) : Notifier {
    private lateinit var computationServiceNotificationBuilder: NotificationCompat.Builder

    override fun updateComputationServiceNotification(progress: Int) {
        val notification = computationServiceNotificationBuilder.apply {
            if (progress < PROGRESS_MAX) {
                setProgress(PROGRESS_MAX, progress, false)
                // On API lower than 34 this will make the notification non-dismissable.
                // On API 34+ the following new behavior is expected:
                // https://developer.android.com/about/versions/14/behavior-changes-all#non-dismissable-notifications
                setOngoing(true)
            } else {
                setContentText(context.getString(R.string.core_notifications_computation_service_computation_finished))
                setProgress(0, 0, false)
                setOngoing(false)
            }
        }.build()
        NotificationManagerCompat.from(context).notify(COMPUTATION_SERVICE_NOTIFICATION_ID, notification)
    }

    override fun createComputationServiceNotification(computationName: String?): Notification {
        ensureComputationServiceNotificationChannelExists()
        computationServiceNotificationBuilder =
            NotificationCompat.Builder(context, COMPUTATION_SERVICE_NOTIFICATION_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.core_notifications_ic_afoil_notifications)
                if (computationName != null) {
                    setContentTitle(computationName)
                    setContentIntent(computationMonitorPendingIntent(computationName))
                }
                // Show notification immediately
                foregroundServiceBehavior = NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
            }
        return computationServiceNotificationBuilder.build()
    }

    private fun ensureComputationServiceNotificationChannelExists() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.core_notifications_computation_service_channel_name)
            val descriptionText = context.getString(R.string.core_notifications_computation_service_channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(
                /* id = */ COMPUTATION_SERVICE_NOTIFICATION_CHANNEL_ID,
                /* name = */ name,
                /* importance = */ importance
            ).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun computationMonitorPendingIntent(
        computationName: String
    ): PendingIntent? {
        val deepLinkIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = "$DEEP_LINK_SCHEME_AND_HOST/$COMPUTATION_MONITOR/$computationName".toUri()
            component = ComponentName(
                /* pkg = */ context.packageName,
                /* cls = */ TARGET_ACTIVITY_NAME,
            )
        }
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(
                /* requestCode = */ COMPUTATION_SERVICE_NOTIFICATION_REQUEST_CODE,
                /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    companion object {
        const val COMPUTATION_SERVICE_NOTIFICATION_ID = 1
    }
}