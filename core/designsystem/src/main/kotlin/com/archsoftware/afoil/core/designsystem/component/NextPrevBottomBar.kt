package com.archsoftware.afoil.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.designsystem.R
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme

@Composable
fun NextPrevBottomBar(
    shouldShowDone: Boolean,
    previousEnabled: Boolean,
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
            enabled = previousEnabled
        ) {
            Text(text = stringResource(id = R.string.core_designsystem_nextprevbottombar_previous))
        }
        Button(onClick = { if (shouldShowDone) onDone() else onNextClick() }) {
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

@Preview
@Composable
private fun NextPrevBottomBarPreview() {
    AfoilTheme {
        NextPrevBottomBar(
            shouldShowDone = false,
            previousEnabled = true,
            onPreviousClick = {},
            onNextClick = {},
            onDone = {}
        )
    }
}