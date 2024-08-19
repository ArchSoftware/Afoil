package com.archsoftware.afoil.computation.service

import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.core.testing.manager.TestAfoilComputationManager
import com.archsoftware.afoil.core.testing.notifications.TestSystemTrayNotifier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ServiceController

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ComputationServiceTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testComputationManager = TestAfoilComputationManager()

    private lateinit var serviceController: ServiceController<ComputationService>
    private lateinit var computationService: ComputationService

    @Before
    fun setup() {
        serviceController = Robolectric.buildService(ComputationService::class.java)
        computationService = serviceController.get()
        computationService.apply {
            notifier = TestSystemTrayNotifier()
            computationManager = testComputationManager
            defaultDispatcher = StandardTestDispatcher(testScheduler)
        }
    }

    @Test
    fun computationIsStartedWhenServiceIsStarted() = runTest(testScheduler) {
        serviceController.startCommand(0, 0)

        assert(testComputationManager.getComputationState().first() == ComputationManager.State.RUNNING)
    }

    @Test
    fun computationAndCollectJobAreCanceledWhenServiceIsStopped() = runTest(testScheduler) {
        serviceController.startCommand(0, 0)
        serviceController.destroy()

        assert(computationService.collectJob.isCancelled)
        assert(testComputationManager.getComputationState().first() == ComputationManager.State.CANCELED)
    }

    @Test
    fun serviceIsAutomaticallyStoppedWhenComputationFinishes() = runTest(testScheduler) {
        serviceController.startCommand(0, 0)
        testComputationManager.sendState(ComputationManager.State.FINISHED)

        advanceUntilIdle()

        assert(Shadows.shadowOf(computationService).isStoppedBySelf)
    }
}