package com.archsoftware.afoil.core.notifications

import android.app.Notification

interface Notifier {
    fun updateComputationServiceNotification(progress: Float)
    fun createComputationServiceNotification(computationName: String?): Notification
}