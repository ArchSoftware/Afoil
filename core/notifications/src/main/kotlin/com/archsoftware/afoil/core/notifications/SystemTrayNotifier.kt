package com.archsoftware.afoil.core.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val COMPUTATION_SERVICE_NOTIFICATION_CHANNEL_ID = "computation_service_channel"
private const val COMPUTATION_SERVICE_NOTIFICATION_ID = 1
private const val PROGRESS_MAX = 100

class SystemTrayNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val computationServiceNotificationId: Int = COMPUTATION_SERVICE_NOTIFICATION_ID
    private lateinit var computationServiceNotificationBuilder: NotificationCompat.Builder

    fun updateComputationServiceNotification(progress: Float) {
        val notification = computationServiceNotificationBuilder.apply {
            if (progress < PROGRESS_MAX) {
                setProgress(PROGRESS_MAX, progress.toInt(), false)
                setOngoing(true)
            } else {
                setContentText(context.getString(R.string.core_notifications_computation_service_computation_finished))
                setProgress(0, 0, false)
                setOngoing(false)
            }
        }.build()
        NotificationManagerCompat.from(context).notify(COMPUTATION_SERVICE_NOTIFICATION_ID, notification)
    }

    fun createComputationServiceNotification(computationName: String?): Notification {
        ensureComputationServiceNotificationChannelExists()
        computationServiceNotificationBuilder =
            NotificationCompat.Builder(context, COMPUTATION_SERVICE_NOTIFICATION_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.core_notifications_ic_afoil_notifications)
                if (computationName != null) setContentTitle(computationName)
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
}