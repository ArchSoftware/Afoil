package com.archsoftware.afoil.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.common.utils.format
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme

/**
 * A composable that displays a slider for accuracy control.
 *
 * @param text The text to display above the slider.
 * @param value The current value of the slider.
 * @param onValueChange The callback to invoke when the slider value changes.
 * @param modifier The [Modifier] to apply to this slider.
 * @param valueRange The range of values that the slider can take.
 * @param supportingText An optional supporting text to display below the slider.
 */
@Composable
fun AccuracySlider(
    text: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    supportingText: String? = null
) {
    Column(modifier = modifier) {
        Row {
            Text(text = text)
            Spacer(modifier = modifier.weight(1f))
            Text(text = value.format(2))
        }
        Slider(
            value = value,
            valueRange = valueRange,
            onValueChange = onValueChange
        )
        if (supportingText != null) {
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AccuracySliderPreview() {
    AfoilTheme {
        AccuracySlider(
            text = "Text",
            value = 0.5f,
            supportingText = "Supporting text",
            onValueChange = {}
        )
    }
}