package com.archsoftware.afoil

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.archsoftware.afoil.core.testing.util.onNodeWithStringId
import com.archsoftware.afoil.navigation.TopLevelDestination
import com.archsoftware.afoil.ui.home.HomeScreen
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun homeScreen_shouldNotShowHomeDestination() {
        composeTestRule.setContent {
            HomeScreen(
                canNavigate = true,
                onDestinationSelected = {}
            )
        }

        var displayedDestinationsCount = 0
        for (destination in TopLevelDestination.entries) {
            val titleId = destination.titleId
            if (titleId != null) {
                composeTestRule.onNodeWithStringId(titleId).assertIsDisplayed()
                displayedDestinationsCount += 1
            }
        }

        assert(displayedDestinationsCount == TopLevelDestination.entries.size - 1)
    }

    @Test
    fun homeScreen_navigationDisabled() {
        composeTestRule.setContent {
            HomeScreen(
                canNavigate = false,
                onDestinationSelected = {}
            )
        }

        for (destination in TopLevelDestination.entries) {
            val titleId = destination.titleId
            if (titleId != null) {
                composeTestRule.onNodeWithStringId(titleId).assertIsNotEnabled()
            }
        }
    }
}