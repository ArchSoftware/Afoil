package com.archsoftware.afoil.feature.computationmonitor

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.archsoftware.afoil.computation.manager.ComputationManager
import com.archsoftware.afoil.core.testing.util.onNodeWithStringId
import org.junit.Rule
import org.junit.Test

class ComputationMonitorScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun computationMonitorScreen_shouldNotShowCancelConfirmationDialog() {
        composeTestRule.setContent {
            ComputationMonitorScreen(
                projectName = "My Project",
                state = ComputationManager.State.IDLE,
                logs = emptyList(),
                progress = 0f,
                showProgressBar = false,
                confirmationDialogRequester = CancelConfirmationDialogRequester.NONE,
                onConfirmationDialogRequest = {},
                onCancelConfirmationDialogConfirm = {},
                onCancelConfirmationDialogDismiss = {},
                onNavigateUp = {},
                onGoToResults = {}
            )
        }

        composeTestRule.onNodeWithTag("cancelConfirmationDialog").assertIsNotDisplayed()
    }

    @Test
    fun computationMonitorScreen_showGoToResultsButtonIfComputationHasFinished() {
        composeTestRule.setContent {
            ComputationMonitorScreen(
                projectName = "My Project",
                state = ComputationManager.State.FINISHED,
                logs = emptyList(),
                progress = 0f,
                showProgressBar = false,
                confirmationDialogRequester = CancelConfirmationDialogRequester.NONE,
                onConfirmationDialogRequest = {},
                onCancelConfirmationDialogConfirm = {},
                onCancelConfirmationDialogDismiss = {},
                onNavigateUp = {},
                onGoToResults = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.feature_computationmonitor_go_to_results)
            .assertIsDisplayed()
    }

    @Test
    fun computationMonitorScreen_shouldNotRequestConfirmationDialogIfComputationHasFinished() {
        var onConfirmationDialogRequestInvoked = false
        var onNavigateUpInvoked = false
        var onGoToResultsInvoked = false

        composeTestRule.setContent {
            ComputationMonitorScreen(
                projectName = "My Project",
                state = ComputationManager.State.FINISHED,
                logs = emptyList(),
                progress = 0f,
                showProgressBar = false,
                confirmationDialogRequester = CancelConfirmationDialogRequester.NONE,
                onConfirmationDialogRequest = { onConfirmationDialogRequestInvoked = true },
                onCancelConfirmationDialogConfirm = {},
                onCancelConfirmationDialogDismiss = {},
                onNavigateUp = { onNavigateUpInvoked = true },
                onGoToResults = { onGoToResultsInvoked = true }
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.feature_computationmonitor_navigateup_content_desc)
            )
            .performClick()
        assert(onNavigateUpInvoked)
        assert(!onConfirmationDialogRequestInvoked)

        composeTestRule
            .onNodeWithStringId(R.string.feature_computationmonitor_go_to_results)
            .performClick()
        assert(onGoToResultsInvoked)
        assert(!onConfirmationDialogRequestInvoked)
    }

    @Test
    fun computationMonitorScreen_showCancelConfirmationDialogOnNavigateUpIfComputationHasNotFinished() {
        composeTestRule.setContent {
            ComputationMonitorScreen(
                projectName = "My Project",
                state = ComputationManager.State.RUNNING,
                logs = emptyList(),
                progress = 0f,
                showProgressBar = false,
                confirmationDialogRequester = CancelConfirmationDialogRequester.NAVIGATE_UP,
                onConfirmationDialogRequest = {},
                onCancelConfirmationDialogConfirm = {},
                onCancelConfirmationDialogDismiss = {},
                onNavigateUp = {},
                onGoToResults = {}
            )
        }

        composeTestRule.onNodeWithTag("cancelConfirmationDialog").assertIsDisplayed()
    }

    @Test
    fun computationMonitorScreen_showCancelConfirmationDialogOnCancelIfComputationHasNotFinished() {
        composeTestRule.setContent {
            ComputationMonitorScreen(
                projectName = "My Project",
                state = ComputationManager.State.RUNNING,
                logs = emptyList(),
                progress = 0f,
                showProgressBar = false,
                confirmationDialogRequester = CancelConfirmationDialogRequester.CANCEL,
                onConfirmationDialogRequest = {},
                onCancelConfirmationDialogConfirm = {},
                onCancelConfirmationDialogDismiss = {},
                onNavigateUp = {},
                onGoToResults = {}
            )
        }

        composeTestRule.onNodeWithTag("cancelConfirmationDialog").assertIsDisplayed()
    }

    @Test
    fun computationMonitorScreen_shouldNotRequestConfirmationDialogIfComputationThrownError() {
        var onConfirmationDialogRequestInvoked = false
        var onNavigateUpInvoked = false

        composeTestRule.setContent {
            ComputationMonitorScreen(
                projectName = "My Project",
                state = ComputationManager.State.ERROR,
                logs = emptyList(),
                progress = 0f,
                showProgressBar = false,
                confirmationDialogRequester = CancelConfirmationDialogRequester.NONE,
                onConfirmationDialogRequest = { onConfirmationDialogRequestInvoked = true },
                onCancelConfirmationDialogConfirm = {},
                onCancelConfirmationDialogDismiss = {},
                onNavigateUp = { onNavigateUpInvoked = true },
                onGoToResults = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.feature_computationmonitor_navigateup_content_desc)
            )
            .performClick()
        assert(onNavigateUpInvoked)
        assert(!onConfirmationDialogRequestInvoked)

        composeTestRule
            .onNodeWithStringId(R.string.feature_computationmonitor_exit)
            .performClick()
        assert(onNavigateUpInvoked)
        assert(!onConfirmationDialogRequestInvoked)
    }

    @Test
    fun computationMonitorScreen_shouldNotRequestConfirmationDialogIfComputationWasCanceled() {
        var onConfirmationDialogRequestInvoked = false
        var onNavigateUpInvoked = false

        composeTestRule.setContent {
            ComputationMonitorScreen(
                projectName = "My Project",
                state = ComputationManager.State.CANCELED,
                logs = emptyList(),
                progress = 0f,
                showProgressBar = false,
                confirmationDialogRequester = CancelConfirmationDialogRequester.NONE,
                onConfirmationDialogRequest = { onConfirmationDialogRequestInvoked = true },
                onCancelConfirmationDialogConfirm = {},
                onCancelConfirmationDialogDismiss = {},
                onNavigateUp = { onNavigateUpInvoked = true },
                onGoToResults = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(R.string.feature_computationmonitor_navigateup_content_desc)
            )
            .performClick()
        assert(onNavigateUpInvoked)
        assert(!onConfirmationDialogRequestInvoked)

        composeTestRule
            .onNodeWithStringId(R.string.feature_computationmonitor_exit)
            .performClick()
        assert(onNavigateUpInvoked)
        assert(!onConfirmationDialogRequestInvoked)
    }
}