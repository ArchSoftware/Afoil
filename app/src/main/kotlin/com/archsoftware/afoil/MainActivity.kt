package com.archsoftware.afoil

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.archsoftware.afoil.computation.service.ComputationService
import com.archsoftware.afoil.core.common.contentresolver.AfoilContentResolver
import com.archsoftware.afoil.core.data.repository.UserPreferencesRepository
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.ui.AfoilApp
import com.archsoftware.afoil.ui.rememberAfoilAppState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @Inject
    lateinit var afoilContentResolver: AfoilContentResolver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val appState = rememberAfoilAppState(
                contentResolver = afoilContentResolver,
                userPreferencesRepository = userPreferencesRepository
            )

            // Request POST_NOTIFICATIONS permission on Android 13+
            NotificationPermissionEffect()

            AfoilTheme {
                AfoilApp(
                    afoilAppState = appState,
                    onProjectSetupDone = { projectName ->
                        appState.navigateToComputationMonitor(projectName)
                        startComputation(projectName)
                    },
                )
            }
        }
    }

    private fun startComputation(projectName: String) {
        val startIntent = ComputationService.createStartIntent(this, projectName)
        ContextCompat.startForegroundService(this, startIntent)
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun NotificationPermissionEffect() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    val notificationsPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )
    LaunchedEffect(notificationsPermissionState) {
        val status = notificationsPermissionState.status
        if (status is PermissionStatus.Denied && !status.shouldShowRationale) {
            notificationsPermissionState.launchPermissionRequest()
        }
    }
}