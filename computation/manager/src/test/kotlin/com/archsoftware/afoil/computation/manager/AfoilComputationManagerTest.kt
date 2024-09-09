package com.archsoftware.afoil.computation.manager

import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import com.archsoftware.afoil.core.testing.datairfoilreader.TestDatAirfoilReader
import com.archsoftware.afoil.core.testing.projectstore.TestAfoilProjectStore
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectDataRepository
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectNumResultRepository
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectPostResultRepository
import com.archsoftware.afoil.core.testing.repository.TestAfoilProjectRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    private val projectId = 1L
    private suspend fun computationWithoutException() = delay(1000)
    private suspend fun computationWithException() {
        delay(1000)
        throw Exception()
    }

    private lateinit var computationManager: AfoilComputationManager

    @Before
    fun setup() {
        computationManager = AfoilComputationManager(
            projectRepository = TestAfoilProjectRepository(),
            projectDataRepository = TestAfoilProjectDataRepository(),
            projectNumResultRepository = TestAfoilProjectNumResultRepository(),
            projectPostResultRepository = TestAfoilProjectPostResultRepository(),
            projectStore = TestAfoilProjectStore(),
            datAirfoilReader = TestDatAirfoilReader(),
            contentResolver = TestAfoilContentResolver(),
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

        computationManager.startComputation(projectId) { computationWithoutException() }
        computationManager.stopComputation(true)
        val state = computationManager.getComputationState().first()

        assertEquals(expectedState, state)
        assertTrue(computationManager.computationJob.isCancelled)
    }

    @Test
    fun computationStateIsFinishedIfComputationIsStoppedWhileNotRunning() = runTest(testScheduler) {
        val expectedState = ComputationManager.State.FINISHED

        val states = mutableListOf<ComputationManager.State>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            computationManager.getComputationState().toList(states)
        }

        computationManager.startComputation(projectId) { computationWithoutException() }

        advanceUntilIdle()

        computationManager.stopComputation(false)

        advanceUntilIdle()

        assertEquals(expectedState, states.last())
        assertTrue(computationManager.computationJob.isCancelled)
    }

    @Test
    fun computationStateIsErrorIfComputationThrowsException() = runTest(testScheduler) {
        val expectedState = ComputationManager.State.ERROR

        computationManager.startComputation(projectId) { computationWithException() }

        advanceUntilIdle()

        val state = computationManager.getComputationState().first()

        assertEquals(expectedState, state)
    }
}