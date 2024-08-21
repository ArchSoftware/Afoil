package com.archsoftware.afoil.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.archsoftware.afoil.core.designsystem.component.NextPrevBottomBar
import com.archsoftware.afoil.core.testing.util.onNodeWithStringId
import org.junit.Rule
import org.junit.Test

class NextPrevBottomBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun nextPrevBottomBar_shouldNotShowDoneButton() {
        var onNextInvoked = false

        composeTestRule.setContent {
            NextPrevBottomBar(
                shouldShowDone = false,
                previousEnabled = false,
                isLoading = false,
                onPreviousClick = {},
                onNextClick = { onNextInvoked = true },
                onDone = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_nextprevbottombar_next)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_nextprevbottombar_next)
            .performClick()
        assert(onNextInvoked)
    }

    @Test
    fun nextPrevBottomBar_shouldShowDoneButton() {
        var onDoneInvoked = false

        composeTestRule.setContent {
            NextPrevBottomBar(
                shouldShowDone = true,
                previousEnabled = false,
                isLoading = false,
                onPreviousClick = {},
                onNextClick = {},
                onDone = { onDoneInvoked = true }
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_nextprevbottombar_done)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_nextprevbottombar_done)
            .performClick()
        assert(onDoneInvoked)
    }

    @Test
    fun nextPrevBottomBar_previousDisabled() {
        composeTestRule.setContent {
            NextPrevBottomBar(
                shouldShowDone = true,
                previousEnabled = false,
                isLoading = false,
                onPreviousClick = {},
                onNextClick = {},
                onDone = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_nextprevbottombar_previous)
            .assertIsNotEnabled()
    }

    @Test
    fun nextPrevBottomBar_previousEnabled() {
        composeTestRule.setContent {
            NextPrevBottomBar(
                shouldShowDone = false,
                previousEnabled = true,
                isLoading = false,
                onPreviousClick = {},
                onNextClick = {},
                onDone = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_nextprevbottombar_previous)
            .assertIsEnabled()
    }

    @Test
    fun nextPrevBottomBar_isLoading() {
        composeTestRule.setContent {
            NextPrevBottomBar(
                shouldShowDone = false,
                previousEnabled = true,
                isLoading = true,
                onPreviousClick = {},
                onNextClick = {},
                onDone = {}
            )
        }

        composeTestRule
            .onNodeWithTag("loadingIndicator")
            .assertIsEnabled()
    }

    @Test
    fun nextPrevBottomBar_isLoadingDisablesNextPrevButtons() {
        composeTestRule.setContent {
            NextPrevBottomBar(
                shouldShowDone = false,
                previousEnabled = true,
                isLoading = true,
                onPreviousClick = {},
                onNextClick = {},
                onDone = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_nextprevbottombar_next)
            .assertIsNotEnabled()
        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_nextprevbottombar_previous)
            .assertIsNotEnabled()
    }
}