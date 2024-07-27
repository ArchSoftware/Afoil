package com.archsoftware.afoil.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import com.archsoftware.afoil.feature.airfoilanalysis.navigation.navigateToAirfoilAnalysis
import com.archsoftware.afoil.navigation.TopLevelDestination
import com.archsoftware.afoil.ui.home.navigation.navigateToHome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Composable
fun rememberAfoilAppState(
    contentResolver: UriContentResolver,
    userPreferencesRepository: PreferencesRepository,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AfoilAppState = remember(
    coroutineScope,
    userPreferencesRepository,
    navController
) {
    AfoilAppState(
        contentResolver = contentResolver,
        coroutineScope = coroutineScope,
        navController = navController,
        userPreferencesRepository = userPreferencesRepository
    )
}

data class AfoilAppState(
    val contentResolver: UriContentResolver,
    val coroutineScope: CoroutineScope,
    val userPreferencesRepository: PreferencesRepository,
    val navController: NavHostController
) {
    val shouldShowProjectsDirSelectionDialog: StateFlow<Boolean> =
        userPreferencesRepository.getAfoilProjectsDirectory().map {
            it == null || !contentResolver.checkIfTreeUriExists(Uri.parse(it))
        }
            .stateIn(
                scope = coroutineScope,
                initialValue = false,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    fun onProjectsDirectorySelected(uri: Uri?) {
        if (uri == null) return

        contentResolver.takePersistableUriPermission(uri)

        coroutineScope.launch {
            userPreferencesRepository.updateAfoilProjectsDirectory(uri.toString())
        }
    }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> {
                navController.navigateToHome(navOptions)
            }
            TopLevelDestination.AIRFOIL_ANALYSIS -> {
                navController.navigateToAirfoilAnalysis(navOptions)
            }
        }
    }
}
