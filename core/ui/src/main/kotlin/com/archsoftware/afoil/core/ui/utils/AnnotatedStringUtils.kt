package com.archsoftware.afoil.core.ui.utils

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle

/**
 * Formats a specific range of the given string as a subscript.
 *
 * @param start The start index of the subscript.
 * @param end The end index of the subscript.
 * @param subscriptStyle The style to apply to the subscript.
 * @return The formatted [AnnotatedString].
 */
@Composable
fun String.subscript(
    start: Int,
    end: Int,
    subscriptStyle: TextStyle = LocalTextStyle.current
): AnnotatedString = buildAnnotatedString {
    append(
        text = this@subscript,
        start = 0,
        end = start
    )
    withStyle(
        style = subscriptStyle.toSpanStyle().copy(
            baselineShift = BaselineShift(-0.25f)
        )
    ) {
        append(
            text = this@subscript,
            start = start,
            end = end
        )
    }
    append(
        text = this@subscript,
        start = end,
        end = this@subscript.length
    )
}