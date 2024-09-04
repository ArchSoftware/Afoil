package com.archsoftware.afoil.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.net.toUri
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import com.archsoftware.afoil.feature.airfoilanalysis.navigation.navigateToAirfoilAnalysis
import com.archsoftware.afoil.feature.computationmonitor.navigation.navigateToComputationMonitor
import com.archsoftware.afoil.feature.computationresults.navigation.navigateToComputationResults
import com.archsoftware.afoil.feature.recentprojects.navigation.navigateToRecentProjects
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
    val showNoProjectsDirSelectedMessage: StateFlow<Boolean> =
        userPreferencesRepository.getAfoilProjectsDirectory().map {
            it == null || !contentResolver.checkIfTreeUriExists(it.toUri())
        }
            .stateIn(
                scope = coroutineScope,
                initialValue = false,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    /**
     * Takes persistable uri permission for the selected projects directory [Uri] and stores it
     * as a user preference.
     *
     * @param uri The selected projects directory [Uri].
     */
    fun onProjectsDirectorySelected(uri: Uri?) {
        if (uri == null) return

        contentResolver.takePersistableUriPermission(uri)

        coroutineScope.launch {
            userPreferencesRepository.updateAfoilProjectsDirectory(uri.toString())
        }
    }

    /**
     * Navigates to the selected [TopLevelDestination] saving the state of the current destination
     * to make it available when navigating back to it.
     *
     * @param topLevelDestination The selected [TopLevelDestination].
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val navOptions = navOptions {
            // Pop up to the start destination in the navigation graph to avoid building up
            // a large backstack of destinations
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Top destination should only have one instance active at a time
            launchSingleTop = true
            // Restore state when re-selecting a previously selected destination
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> {
                navController.navigateToHome(navOptions)
            }
            TopLevelDestination.AIRFOIL_ANALYSIS -> {
                navController.navigateToAirfoilAnalysis(navOptions)
            }
            TopLevelDestination.RECENT_PROJECTS -> {
                navController.navigateToRecentProjects(navOptions)
            }
        }
    }

    /**
     * Navigates to the computation monitor screen passing the given [projectId].
     */
    fun navigateToComputationMonitor(projectId: Long) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }

        navController.navigateToComputationMonitor(projectId, navOptions)
    }

    /**
     * Navigates to the computation results screen passing the given [projectId].
     */
    fun navigateToComputationResults(projectId: Long) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }

        navController.navigateToComputationResults(projectId, navOptions)
    }
}
