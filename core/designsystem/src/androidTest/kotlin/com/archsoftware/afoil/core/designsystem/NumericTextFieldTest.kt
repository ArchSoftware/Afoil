package com.archsoftware.afoil.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.archsoftware.afoil.core.designsystem.component.NumericTextField
import com.archsoftware.afoil.core.testing.util.onNodeWithStringId
import org.junit.Rule
import org.junit.Test

class NumericTextFieldTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Test data
    private val value = "Value"
    private val label = "Label"
    private val unitOfMeasure = "unit"
    private val supportingText = "Supporting text"

    @Test
    fun numericTextField_shouldNotShowSupportingText() {
        composeTestRule.setContent {
            NumericTextField(
                value = value,
                label = label,
                onValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithTag("supportingText")
            .assertIsNotDisplayed()
    }

    @Test
    fun numericTextField_shouldShowSupportingText() {
        composeTestRule.setContent {
            NumericTextField(
                value = value,
                label = label,
                onValueChange = {},
                supportingText = supportingText
            )
        }

        composeTestRule
            .onNodeWithText(supportingText)
            .assertIsDisplayed()
    }

    @Test
    fun numericTextField_shouldNotShowErrorMessage() {
        composeTestRule.setContent {
            NumericTextField(
                value = value,
                label = label,
                onValueChange = {},
                isError = false
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_numerictextfield_error_message)
            .assertIsNotDisplayed()
    }

    @Test
    fun numericTextField_shouldShowErrorMessage() {
        composeTestRule.setContent {
            NumericTextField(
                value = value,
                label = label,
                onValueChange = {},
                isError = true
            )
        }

        composeTestRule
            .onNodeWithTag("supportingText")
            .assertIsNotDisplayed()
        composeTestRule
            .onNodeWithStringId(R.string.core_designsystem_numerictextfield_error_message)
            .assertIsDisplayed()
    }

    @Test
    fun numericTextField_shouldNotShowUnitOfMeasure() {
        composeTestRule.setContent {
            NumericTextField(
                value = value,
                label = label,
                onValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithText(unitOfMeasure)
            .assertIsNotDisplayed()
    }

    @Test
    fun numericTextField_shouldShowUnitOfMeasure() {
        composeTestRule.setContent {
            NumericTextField(
                value = value,
                label = label,
                onValueChange = {},
                unitOfMeasure = unitOfMeasure
            )
        }

        composeTestRule
            .onNodeWithText(unitOfMeasure)
            .assertIsDisplayed()
    }
}