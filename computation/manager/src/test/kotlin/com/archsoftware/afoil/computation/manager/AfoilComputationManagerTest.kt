package com.archsoftware.afoil.computation.manager

import com.archsoftware.afoil.core.testing.projectstore.TestAfoilProjectStore
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class AfoilComputationManagerTest {

    private val testScheduler = TestCoroutineScheduler()

    // Test data
    private val projectName = "My Project"

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

        computationManager.startComputation(null)
        assertEquals(expectedState, computationManager.getComputationState().first())
    }

    @Test
    fun computationStateIsCanceledIfComputationIsStopped() = runTest(testScheduler) {
        val expectedState = ComputationManager.State.CANCELED

        computationManager.startComputation(projectName)
        computationManager.stopComputation()
        val state = computationManager.getComputationState().first()

        assertEquals(expectedState, state)
        assertTrue(computationManager.computationJob.isCancelled)
    }
}