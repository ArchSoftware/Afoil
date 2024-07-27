package com.archsoftware.afoil.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.archsoftware.afoil.core.ui.ProjectsDirSelectionDialog
import com.archsoftware.afoil.navigation.AfoilNavHost

@Composable
fun AfoilApp(
    afoilAppState: AfoilAppState,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        var showProjectsDirSelectionDialog by remember { mutableStateOf(false) }
        val selectProjectsDirLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocumentTree()
        ) { uri ->
            afoilAppState.onProjectsDirectorySelected(uri)
            showProjectsDirSelectionDialog = false
        }
        val shouldShowProjectsDirSelectionDialog by afoilAppState
            .shouldShowProjectsDirSelectionDialog
            .collectAsStateWithLifecycle()

        LaunchedEffect(shouldShowProjectsDirSelectionDialog) {
            if (shouldShowProjectsDirSelectionDialog) {
                showProjectsDirSelectionDialog = true
            }
        }

        AfoilApp(
            afoilAppState = afoilAppState,
            showProjectsDirSelectionDialog = showProjectsDirSelectionDialog,
            shouldShowProjectsDirSelectionDialog = shouldShowProjectsDirSelectionDialog,
            onProjectsDirSelectionDialogDismiss = { showProjectsDirSelectionDialog = false },
            onSelectProjectsDir = { selectProjectsDirLauncher.launch(null) })
    }
}

@Composable
internal fun AfoilApp(
    afoilAppState: AfoilAppState,
    showProjectsDirSelectionDialog: Boolean,
    shouldShowProjectsDirSelectionDialog: Boolean,
    onProjectsDirSelectionDialogDismiss: () -> Unit,
    onSelectProjectsDir: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (showProjectsDirSelectionDialog) {
        ProjectsDirSelectionDialog(
            onDismissRequest = onProjectsDirSelectionDialogDismiss,
            onSelectProjectsDir = onSelectProjectsDir,
            modifier = Modifier.testTag("projectsDirSelectionDialog")
        )
    }

    AfoilNavHost(
        canNavigate = !shouldShowProjectsDirSelectionDialog,
        appState = afoilAppState,
        modifier = modifier
    )
}