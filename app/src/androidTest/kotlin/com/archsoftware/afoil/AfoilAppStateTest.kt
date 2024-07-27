package com.archsoftware.afoil

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import com.archsoftware.afoil.core.testing.repository.TestUserPreferencesRepository
import com.archsoftware.afoil.ui.AfoilAppState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class AfoilAppStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val afoilContentResolver = TestAfoilContentResolver()
    private val userPreferencesRepository = TestUserPreferencesRepository()

    private lateinit var state: AfoilAppState

    @Test
    fun afoilAppState_shouldShowProjectsDirSelectionDialog() = runTest(UnconfinedTestDispatcher()) {
        composeTestRule.setContent {
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()

            state = AfoilAppState(
                contentResolver = afoilContentResolver,
                coroutineScope = coroutineScope,
                userPreferencesRepository = userPreferencesRepository,
                navController = navController
            )
        }

        // Create an empty collector for the StateFlow
        backgroundScope.launch { state.shouldShowProjectsDirSelectionDialog.collect() }

        state.onProjectsDirectorySelected(afoilContentResolver.testUri)
        assertTrue(state.shouldShowProjectsDirSelectionDialog.value)

        afoilContentResolver.exists = true
        state.onProjectsDirectorySelected(afoilContentResolver.testUri)
        assertFalse(state.shouldShowProjectsDirSelectionDialog.value)
    }
}