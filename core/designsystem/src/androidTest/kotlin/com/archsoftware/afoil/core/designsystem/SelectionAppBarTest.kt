package com.archsoftware.afoil.core.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.archsoftware.afoil.core.designsystem.component.SelectionAppBar
import org.junit.Rule
import org.junit.Test

class SelectionAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test data
    private val title = "Title"
    private val navIcon = Icons.AutoMirrored.Filled.ArrowBack
    private val navIconContentDesc = ""
    private val selectedCount = 1

    @Test
    fun selectionAppBar_shouldNotShowActionBar() {
        composeTestRule.setContent {
            SelectionAppBar(
                title = title,
                navIcon = navIcon,
                navIconContentDescription = navIconContentDesc,
                selectedCount = 0,
                showActionBar = false,
                onNavIconClick = {},
                onCancelIconClick = {}
            ) {}
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun selectionAppBar_shouldShowActionBar() {
        composeTestRule.setContent {
            SelectionAppBar(
                title = "",
                navIcon = navIcon,
                navIconContentDescription = navIconContentDesc,
                selectedCount = selectedCount,
                showActionBar = true,
                onNavIconClick = {},
                onCancelIconClick = {}
            ) {}
        }

        composeTestRule.onNodeWithText(title).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(selectedCount.toString()).assertIsDisplayed()
    }
}