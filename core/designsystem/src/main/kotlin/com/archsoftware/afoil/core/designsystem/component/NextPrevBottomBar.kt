package com.archsoftware.afoil.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
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
 * Afoil bottom bar with next and previous buttons.
 *
 * @param shouldShowDone Whether the done button should be shown.
 * @param previousEnabled Whether the previous button should be enabled.
 * @param isLoading Whether the loading indicator should be shown.
 * @param onPreviousClick The action to perform when the previous button is clicked.
 * @param onNextClick The action to perform when the next button is clicked.
 * @param onDone The action to perform when the done button is clicked.
 * @param modifier The modifier to be applied to this bottom bar.
 */
@Composable
fun NextPrevBottomBar(
    shouldShowDone: Boolean,
    previousEnabled: Boolean,
    isLoading: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = onPreviousClick,
            enabled = previousEnabled && !isLoading
        ) {
            Text(text = stringResource(id = R.string.core_designsystem_nextprevbottombar_previous))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(32.dp)
                        .testTag("loadingIndicator")
                )
            }
            Button(
                onClick = { if (shouldShowDone) onDone() else onNextClick() },
                enabled = !isLoading
            ) {
                Text(
                    text = stringResource(
                        id = if (shouldShowDone) {
                            R.string.core_designsystem_nextprevbottombar_done
                        } else {
                            R.string.core_designsystem_nextprevbottombar_next
                        }
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun NextPrevBottomBarPreview() {
    AfoilTheme {
        NextPrevBottomBar(
            shouldShowDone = false,
            previousEnabled = true,
            isLoading = true,
            onPreviousClick = {},
            onNextClick = {},
            onDone = {}
        )
    }
}