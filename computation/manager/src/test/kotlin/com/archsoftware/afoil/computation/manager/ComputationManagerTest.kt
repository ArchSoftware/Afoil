package com.archsoftware.afoil.computation.manager

import com.archsoftware.afoil.core.projectstore.ProjectStore
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectRepository
import com.archsoftware.afoil.core.testing.repository.TestUserPreferencesRepository
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
class ComputationManagerTest {

    private val testScheduler = TestCoroutineScheduler()

    // Test data
    private val projectName = "My Project"

    private lateinit var computationManager: ComputationManager

    @Before
    fun setup() {
        computationManager = ComputationManager(
            projectRepository = TestAfoilProjectRepository(),
            projectStore = ProjectStore(
                contentResolver = TestAfoilContentResolver(),
                preferencesRepository = TestUserPreferencesRepository(),
                ioDispatcher = StandardTestDispatcher()
            ),
            defaultDispatcher = StandardTestDispatcher(testScheduler)
        )
    }

    @Test
    fun computationStateIsErrorIfDataIsInvalid() = runTest(testScheduler) {
        val expectedState = ComputationManager.State.ERROR

        computationManager.startComputation(null)
        assertEquals(expectedState, computationManager.computationState.first())
    }

    @Test
    fun computationStateIsCanceledIfComputationIsStopped() = runTest {
        val expectedState = ComputationManager.State.CANCELED

        computationManager.startComputation(projectName)
        computationManager.stopComputation()
        val state = computationManager.computationState.first()

        assertEquals(expectedState, state)
        assertTrue(computationManager.computationJob.isCancelled)
    }
}