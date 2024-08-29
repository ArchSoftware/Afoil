package com.archsoftware.afoil.feature.computationmonitor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.archsoftware.afoil.feature.computationmonitor.ComputationMonitorScreen

const val PROJECT_ID_ARG = "projectId"
const val COMPUTATION_MONITOR_ROUTE = "computationmonitor_route"
private const val DEEP_LINK_URI_PATTERN = "https://www.afoil.archsoftware.com/computationmonitor/{$PROJECT_ID_ARG}"

fun NavController.navigateToComputationMonitor(
    projectId: Long,
    navOptions: NavOptions? = null
) = navigate(
    route = "$COMPUTATION_MONITOR_ROUTE/$projectId",
    navOptions = navOptions
)

fun NavGraphBuilder.computationMonitorScreen(
    onNavigateUp: () -> Unit,
    onCancel: () -> Unit,
    onGoToResults: (projectId: Long) -> Unit
) {
    composable(
        route = "$COMPUTATION_MONITOR_ROUTE/{$PROJECT_ID_ARG}",
        arguments = listOf(navArgument(PROJECT_ID_ARG) { type = NavType.LongType }),
        deepLinks = listOf(navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN })
    ) {
        ComputationMonitorScreen(
            onNavigateUp = onNavigateUp,
            onCancel = onCancel,
            onGoToResults = onGoToResults
        )
    }
}