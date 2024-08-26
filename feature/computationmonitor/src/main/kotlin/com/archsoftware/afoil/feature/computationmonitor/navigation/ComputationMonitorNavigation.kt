package com.archsoftware.afoil.feature.computationmonitor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.archsoftware.afoil.feature.computationmonitor.ComputationMonitorScreen

const val PROJECT_NAME_ARG = "projectName"
const val COMPUTATION_MONITOR_ROUTE = "computationmonitor_route"
private const val DEEP_LINK_URI_PATTERN = "https://www.afoil.archsoftware.com/computationmonitor/{$PROJECT_NAME_ARG}"

fun NavController.navigateToComputationMonitor(
    projectName: String,
    navOptions: NavOptions? = null
) = navigate(
    route = "$COMPUTATION_MONITOR_ROUTE/$projectName",
    navOptions = navOptions
)

fun NavGraphBuilder.computationMonitorScreen(
    onNavigateUp: () -> Unit,
    onCancel: () -> Unit,
    onGoToResults: (projectName: String) -> Unit
) {
    composable(
        route = "$COMPUTATION_MONITOR_ROUTE/{$PROJECT_NAME_ARG}",
        arguments = listOf(navArgument(PROJECT_NAME_ARG) { type = NavType.StringType }),
        deepLinks = listOf(navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN })
    ) {
        ComputationMonitorScreen(
            onNavigateUp = onNavigateUp,
            onCancel = onCancel,
            onGoToResults = onGoToResults
        )
    }
}