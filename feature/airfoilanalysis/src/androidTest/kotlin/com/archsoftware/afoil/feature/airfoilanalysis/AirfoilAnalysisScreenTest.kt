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
    fun airfoilAnalysisScreen_shouldShowProgressIndicatorOnProjectPreparing() {
        composeTestRule.setContent {
            AirfoilAnalysisScreen(
                isProjectPreparing = true,
                currentPage = AirfoilAnalysisPage.POST_PROCESSING_SETTINGS,
                shouldShowDone = true,
                previousEnabled = true,
                snackbarMessage = null,
                onSnackbarShown = {},
                onPreviousClick = {},
                onNextClick = {},
                onNavigateUp = {},
                onDone = {},
            ) {}
        }

        composeTestRule
            .onNodeWithTag("progressIndicator")
            .assertIsDisplayed()
    }
}