package com.archsoftware.afoil

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import com.archsoftware.afoil.core.testing.repository.TestUserPreferencesRepository
import com.archsoftware.afoil.ui.AfoilApp
import com.archsoftware.afoil.ui.rememberAfoilAppState
import org.junit.Rule
import org.junit.Test

class AfoilAppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val afoilContentResolver = TestAfoilContentResolver()
    private val userPreferencesRepository = TestUserPreferencesRepository()

    @Test
    fun afoilApp_shouldShowProjectsDirSelectionDialog() {
        composeTestRule.setContent {
            AfoilApp(
                afoilAppState = rememberAfoilAppState(
                    contentResolver = afoilContentResolver,
                    userPreferencesRepository = userPreferencesRepository
                ),
                showProjectsDirSelectionDialog = true,
                shouldShowProjectsDirSelectionDialog = true,
                onProjectsDirSelectionDialogDismiss = {},
                onSelectProjectsDir = {},
            )
        }

        composeTestRule.onNodeWithTag("projectsDirSelectionDialog").assertIsDisplayed()
    }
}