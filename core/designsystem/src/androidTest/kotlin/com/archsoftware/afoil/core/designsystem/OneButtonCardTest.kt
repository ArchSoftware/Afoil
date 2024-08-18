package com.archsoftware.afoil.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTopPositionInRootIsEqualTo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.designsystem.component.OneButtonCard
import org.junit.Rule
import org.junit.Test

class OneButtonCardTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun oneButtonCard_shouldShowNotDismissButton() {
        composeTestRule.setContent {
            OneButtonCard(
                text = "Some long description",
                buttonText = "Some action",
                showDismissButton = false,
                onButtonClick = {},
                onDismiss = {}
            )
        }

        composeTestRule.onNodeWithTag("dismissIconButton").assertIsNotDisplayed()
        composeTestRule
            .onNodeWithText("Some long description")
            .assertTopPositionInRootIsEqualTo(16.dp)
    }

    @Test
    fun oneButtonCard_shouldShowDismissButton() {
        composeTestRule.setContent {
            OneButtonCard(
                text = "Some long description",
                buttonText = "Some action",
                showDismissButton = true,
                onButtonClick = {},
                onDismiss = {}
            )
        }

        composeTestRule.onNodeWithTag("dismissIconButton").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Some long description")
            .assertTopPositionInRootIsEqualTo(48.dp)
    }
}