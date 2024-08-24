package com.archsoftware.afoil.feature.computationmonitor

import androidx.lifecycle.SavedStateHandle
import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.core.testing.manager.TestAfoilComputationManager
import com.archsoftware.afoil.core.testing.util.MainDispatcherRule
import com.archsoftware.afoil.feature.computationmonitor.navigation.PROJECT_NAME_ARG
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ComputationMonitorViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    
    private val computationManager = TestAfoilComputationManager()
    
    // Test data
    private val projectName = "My Project"

    private lateinit var viewModel: ComputationMonitorViewModel

    @Before
    fun setup() {
        viewModel = ComputationMonitorViewModel(
            computationManager = computationManager,
            savedStateHandle = SavedStateHandle(mapOf(PROJECT_NAME_ARG to projectName))
        )
    }

    @Test
    fun shouldShowProgressBarOnlyIfComputationIsRunning() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.showProgressBar.collect()
        }

        computationManager.sendState(ComputationManager.State.ERROR)
        assert(!viewModel.showProgressBar.value)

        computationManager.sendState(ComputationManager.State.CANCELED)
        assert(!viewModel.showProgressBar.value)

        computationManager.sendState(ComputationManager.State.FINISHED)
        assert(!viewModel.showProgressBar.value)

        computationManager.sendState(ComputationManager.State.RUNNING)
        assert(viewModel.showProgressBar.value)

        collectJob.cancel()
    }
}