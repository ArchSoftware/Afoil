package com.archsoftware.afoil.core.ui.utils

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle

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