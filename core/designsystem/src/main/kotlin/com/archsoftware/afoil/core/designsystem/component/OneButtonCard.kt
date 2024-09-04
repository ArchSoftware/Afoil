package com.archsoftware.afoil.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.designsystem.R
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme

/**
 * Afoil [Card] with a single button.
 *
 * @param text The text to display.
 * @param buttonText The text to display on the button.
 * @param onButtonClick The action to perform when the button is clicked.
 * @param modifier The modifier to be applied to this card.
 * @param showDismissButton Whether to show a dismiss button.
 * @param onDismiss The action to perform when the dismiss button is clicked.
 */
@Composable
fun OneButtonCard(
    text: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDismissButton: Boolean = false,
    onDismiss: () -> Unit = {}
) {
    Card(modifier = modifier.fillMaxWidth()) {
        if (showDismissButton) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.Start)
                    .testTag("dismissIconButton")
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(
                        id = R.string.core_designsystem_onebuttoncard_close_content_desc
                    )
                )
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(
                top = if (showDismissButton) 0.dp else 16.dp,
                start = 16.dp,
                end = 16.dp
            )
        )
        Button(
            onClick = onButtonClick,
            modifier = modifier
                .padding(16.dp)
                .align(Alignment.End)
        ) {
            Text(text = buttonText)
        }
    }
}

@Preview
@Composable
private fun OneButtonCardPreview() {
    AfoilTheme {
        OneButtonCard(
            text = "Some long description",
            buttonText = "Some action",
            showDismissButton = true,
            onButtonClick = {},
            onDismiss = {}
        )
    }
}