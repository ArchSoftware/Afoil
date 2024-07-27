package com.archsoftware.afoil.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProjectsDirSelectionDialog(
    onDismissRequest: () -> Unit,
    onSelectProjectsDir: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onSelectProjectsDir) {
                Text(
                    text = stringResource(id = R.string.core_ui_projectsdirselectiondialog_select)
                )
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.core_ui_projectsdirselectiondialog_title)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.core_ui_projectsdirselectiondialog_text)
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun ProjectsDirSelectionDialogPreview() {
    ProjectsDirSelectionDialog(
        onDismissRequest = {},
        onSelectProjectsDir = {}
    )
}