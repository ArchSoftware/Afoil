package com.archsoftware.afoil.computation.manager

import com.archsoftware.afoil.core.testing.projectstore.TestAfoilProjectStore
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AfoilComputationManagerTest {

    private val testScheduler = TestCoroutineScheduler()

    // Test data
    private val projectName = "My Project"
    private suspend fun computation() = delay(1000)

    private lateinit var computationManager: AfoilComputationManager

    @Before
    fun setup() {
        computationManager = AfoilComputationManager(
            projectRepository = TestAfoilProjectRepository(),
            projectStore = TestAfoilProjectStore(),
            defaultDispatcher = StandardTestDispatcher(testScheduler)
        )
    }

    @Test
    fun computationStateIsErrorIfDataIsInvalid() = runTest(testScheduler) {
        val expectedState = ComputationManager.State.ERROR

        computationManager.startComputation(null) {}
        assertEquals(expectedState, computationManager.getComputationState().first())
    }

    @Test
    fun computationStateIsCanceledIfComputationIsStoppedWhileRunning() = runTest(testScheduler) {
        val expectedState = ComputationManager.State.CANCELED

        computationManager.startComputation(projectName) { computation() }
        computationManager.stopComputation(true)
        val state = computationManager.getComputationState().first()

        assertEquals(expectedState, state)
        assertTrue(computationManager.computationJob.isCancelled)
    }

    @Test
    fun computationStateIsFinishedIfComputationIsStoppedWhileNotRunning() = runTest(testScheduler) {
        val expectedState = ComputationManager.State.FINISHED

        computationManager.startComputation(projectName) { computation() }

        advanceUntilIdle()

        computationManager.stopComputation(false)

        advanceUntilIdle()

        val state = computationManager.getComputationState().first()

        assertEquals(expectedState, state)
        assertTrue(computationManager.computationJob.isCancelled)
    }
}