package com.archsoftware.afoil.core.designsystem.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.archsoftware.afoil.core.designsystem.R
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme

@Composable
fun NumericTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    unitOfMeasure: String? = null,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        suffix = {
            if (unitOfMeasure != null) {
                Text(text = unitOfMeasure)
            }
        },
        supportingText = {
            if (isError) {
                Text(
                    text = stringResource(id = R.string.core_designsystem_numerictextfield_error_message),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                if (supportingText != null) {
                    Text(
                        text = supportingText,
                        modifier = Modifier.testTag("supportingText")
                    )
                }
            }
        },
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        modifier = modifier
    )
}

@Preview
@Composable
private fun NumericTextFieldPreview() {
    AfoilTheme {
        NumericTextField(
            value = "Value",
            label = "Label",
            onValueChange = {},
            supportingText = "Supporting text",
            unitOfMeasure = "unit"
        )
    }
}