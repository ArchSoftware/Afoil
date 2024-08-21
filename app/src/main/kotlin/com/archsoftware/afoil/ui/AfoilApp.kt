package com.archsoftware.afoil.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.archsoftware.afoil.R
import com.archsoftware.afoil.core.designsystem.component.OneButtonCard
import com.archsoftware.afoil.navigation.AfoilNavHost

@Composable
fun AfoilApp(
    afoilAppState: AfoilAppState,
    onProjectSetupDone: (projectName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        val selectProjectsDirLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocumentTree()
        ) { uri ->
            afoilAppState.onProjectsDirectorySelected(uri)
        }
        val showNoProjectsDirSelectedMessage by afoilAppState
            .showNoProjectsDirSelectedMessage
            .collectAsStateWithLifecycle()

        AfoilApp(
            afoilAppState = afoilAppState,
            showNoProjectsDirSelectedMessage = showNoProjectsDirSelectedMessage,
            onSelectProjectsDir = { selectProjectsDirLauncher.launch(null) },
            onProjectSetupDone = onProjectSetupDone,
        )
    }
}

@Composable
internal fun AfoilApp(
    afoilAppState: AfoilAppState,
    showNoProjectsDirSelectedMessage: Boolean,
    onSelectProjectsDir: () -> Unit,
    onProjectSetupDone: (projectName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AfoilNavHost(
            canNavigate = !showNoProjectsDirSelectedMessage,
            appState = afoilAppState,
            onProjectSetupDone = onProjectSetupDone,
        )
        if (showNoProjectsDirSelectedMessage) {
            OneButtonCard(
                text = stringResource(id = R.string.no_projects_dir_selected_message),
                buttonText = stringResource(id = R.string.select_directory),
                onButtonClick = onSelectProjectsDir,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .windowInsetsPadding(WindowInsets.systemBars)
            )
        }
    }
}