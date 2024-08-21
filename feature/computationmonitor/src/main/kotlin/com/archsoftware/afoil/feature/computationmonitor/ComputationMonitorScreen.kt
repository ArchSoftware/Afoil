package com.archsoftware.afoil.feature.computationmonitor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.designsystem.util.freeScroll
import com.archsoftware.afoil.core.designsystem.util.rememberFreeScrollState
import com.archsoftware.afoil.core.model.ComputationLog
import com.archsoftware.afoil.core.ui.ComputationLogRow

@Composable
fun ComputationMonitorScreen(
    onNavigateUp: () -> Unit,
    onCancel: () -> Unit,
    onGoToResults: (projectName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ComputationMonitorViewModel = hiltViewModel()
) {
    val logs by viewModel.logs.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val isComputationFinished by viewModel.isComputationFinished.collectAsStateWithLifecycle()
    val showProgressBar by viewModel.showProgressBar.collectAsStateWithLifecycle()
    var confirmationDialogRequester by remember { mutableStateOf(CancelConfirmationDialogRequester.NONE) }

    ComputationMonitorScreen(
        projectName = viewModel.projectName,
        logs = logs,
        progress = progress,
        isComputationFinished = isComputationFinished,
        showProgressBar = showProgressBar,
        confirmationDialogRequester = confirmationDialogRequester,
        onConfirmationDialogRequest = { requester ->
            confirmationDialogRequester = requester
        },
        onCancelConfirmationDialogConfirm = {
            when (confirmationDialogRequester) {
                CancelConfirmationDialogRequester.BACK_PRESS,
                CancelConfirmationDialogRequester.NAVIGATE_UP -> {
                    onCancel()
                    onNavigateUp()
                }
                CancelConfirmationDialogRequester.CANCEL -> {
                    onCancel()
                }
                else -> {}
            }
            confirmationDialogRequester = CancelConfirmationDialogRequester.NONE
        },
        onCancelConfirmationDialogDismiss = {
            confirmationDialogRequester = CancelConfirmationDialogRequester.NONE
        },
        onNavigateUp = onNavigateUp,
        onGoToResults = { onGoToResults(viewModel.projectName) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ComputationMonitorScreen(
    projectName: String,
    logs: List<ComputationLog>,
    progress: Float,
    isComputationFinished: Boolean,
    showProgressBar: Boolean,
    confirmationDialogRequester: CancelConfirmationDialogRequester,
    onConfirmationDialogRequest: (requester: CancelConfirmationDialogRequester) -> Unit,
    onCancelConfirmationDialogConfirm: () -> Unit,
    onCancelConfirmationDialogDismiss: () -> Unit,
    onNavigateUp: () -> Unit,
    onGoToResults: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "progressBarAnimation"
    )
    val freeScrollState = rememberFreeScrollState()

    BackHandler(!isComputationFinished) {
        onConfirmationDialogRequest(CancelConfirmationDialogRequester.BACK_PRESS)
    }

    if (confirmationDialogRequester != CancelConfirmationDialogRequester.NONE) {
        AlertDialog(
            title = {
                Text(
                    text = stringResource(
                        id = R.string.feature_computationmonitor_cancelconfirmationdialog_title
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.feature_computationmonitor_cancelconfirmationdialog_message
                    )
                )
            },
            onDismissRequest = onCancelConfirmationDialogDismiss,
            confirmButton = {
                Button(onClick = onCancelConfirmationDialogConfirm) {
                    Text(text = stringResource(id = R.string.feature_computationmonitor_confirm))
                }
            },
            dismissButton = {
                Button(onClick = onCancelConfirmationDialogDismiss) {
                    Text(text = stringResource(id = R.string.feature_computationmonitor_cancel))
                }
            },
            modifier = Modifier.testTag("cancelConfirmationDialog")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = projectName) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isComputationFinished) {
                                onNavigateUp()
                            } else {
                                onConfirmationDialogRequest(CancelConfirmationDialogRequester.NAVIGATE_UP)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.feature_computationmonitor_navigateup_content_desc
                            )
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column {
                HorizontalDivider()
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if (isComputationFinished) {
                                onGoToResults()
                            } else {
                                onConfirmationDialogRequest(CancelConfirmationDialogRequester.CANCEL)
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(
                                id = if (isComputationFinished) {
                                    R.string.feature_computationmonitor_go_to_results
                                } else {
                                    R.string.feature_computationmonitor_cancel
                                }
                            )
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            AnimatedVisibility(visible = showProgressBar) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(
                text = stringResource(id = R.string.feature_computationmonitor_tip),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                    .freeScroll(state = freeScrollState)
            ) {
                logs.forEach { log ->
                    ComputationLogRow(log = log)
                }
            }
        }
    }
}

internal enum class CancelConfirmationDialogRequester {
    NONE,
    BACK_PRESS,
    NAVIGATE_UP,
    CANCEL
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ComputationMonitorScreenPreview() {
    AfoilTheme {
        ComputationMonitorScreen(
            projectName = "My Project",
            logs = buildList {
                repeat(10) {
                    add(
                        ComputationLog(
                            timestamp = "2023-01-01 12:00:00",
                            tag = "ComputationLog",
                            message = "This is a computation log message.",
                            level = ComputationLog.Level.INFO
                        )
                    )
                }
            },
            progress = 0.5f,
            isComputationFinished = false,
            showProgressBar = true,
            confirmationDialogRequester = CancelConfirmationDialogRequester.NONE,
            onConfirmationDialogRequest = {},
            onCancelConfirmationDialogConfirm = {},
            onCancelConfirmationDialogDismiss = {},
            onNavigateUp = {},
            onGoToResults = {}
        )
    }
}