package com.archsoftware.afoil.feature.airfoilanalysis

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class AirfoilAnalysisScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun airfoilAnalysisScreen_shouldShowDialogOnProjectPreparing() {
        composeTestRule.setContent {
            AirfoilAnalysisScreen(
                showProjectPreparingDialog = true,
                projectName = "My projects",
                currentPage = AirfoilAnalysisPage.POST_PROCESSING_SETTINGS,
                shouldShowDone = true,
                previousEnabled = true,
                onPreviousClick = {},
                onNextClick = {},
                onNavigateUp = {},
                onDone = {},
                onProjectPreparingDialogDismiss = {}
            ) {}
        }

        composeTestRule
            .onNodeWithTag("projectPreparingDialog")
            .assertIsDisplayed()
    }
}