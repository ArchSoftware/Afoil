package com.archsoftware.afoil.core.testing.util

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.rules.TestRule

/**
 * Finds a node with the given string resource ID as text.
 */
fun <R: TestRule, A: ComponentActivity> AndroidComposeTestRule<R, A>.onNodeWithStringId(
    @StringRes id: Int
) = onNodeWithText(activity.getString(id))
