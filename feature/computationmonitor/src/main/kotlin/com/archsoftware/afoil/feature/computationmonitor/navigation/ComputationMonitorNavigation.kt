package com.archsoftware.afoil.feature.computationmonitor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.archsoftware.afoil.feature.computationmonitor.ComputationMonitorScreen

const val PROJECT_NAME_ARG = "projectName"
const val COMPUTATION_MONITOR_ROUTE = "computationmonitor_route"

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
        arguments = listOf(navArgument(PROJECT_NAME_ARG) { type = NavType.StringType })
    ) {
        ComputationMonitorScreen(
            onNavigateUp = onNavigateUp,
            onCancel = onCancel,
            onGoToResults = onGoToResults
        )
    }
}