package com.archsoftware.afoil.core.testing.notifications

import android.app.Notification
import com.archsoftware.afoil.core.notifications.Notifier

/**
 * Test-only implementation of the [Notifier] interface.
 */
class TestSystemTrayNotifier : Notifier {
    override fun updateComputationServiceNotification(progress: Int) {}

    override fun createComputationServiceNotification(projectId: Long?, computationName: String?): Notification {
        return Notification()
    }
}