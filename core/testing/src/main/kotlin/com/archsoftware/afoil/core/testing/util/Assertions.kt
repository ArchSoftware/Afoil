package com.archsoftware.afoil.core.testing.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.text.TextLayoutResult

/**
 * Asserts that the provided semantics text node is of the expected color.
 */
fun SemanticsNodeInteraction.assertTextIsOfColor(
    expected: Color
): SemanticsNodeInteraction = assert(
    SemanticsMatcher(
        description = "${SemanticsProperties.Text.name} is of color $expected"
    ) { node ->
        val textLayoutResults = mutableListOf<TextLayoutResult>()
        node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(textLayoutResults)
        if (textLayoutResults.isEmpty()) return@SemanticsMatcher false
        return@SemanticsMatcher textLayoutResults.first().layoutInput.style.color == expected
    }
)