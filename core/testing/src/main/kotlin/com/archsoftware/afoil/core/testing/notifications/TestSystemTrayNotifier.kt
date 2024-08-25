package com.archsoftware.afoil.core.testing.notifications

import android.app.Notification
import com.archsoftware.afoil.core.notifications.Notifier

class TestSystemTrayNotifier : Notifier {
    override fun updateComputationServiceNotification(progress: Int) {}

    override fun createComputationServiceNotification(computationName: String?): Notification {
        return Notification()
    }
}