package com.archsoftware.afoil.feature.computationresults.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.archsoftware.afoil.feature.computationresults.ComputationResultsScreen

const val PROJECT_ID_ARG = "projectId"
const val COMPUTATION_RESULTS_ROUTE = "computationresults_route"

fun NavController.navigateToComputationResults(
    projectId: Long,
    navOptions: NavOptions? = null
) = navigate(
    route = "$COMPUTATION_RESULTS_ROUTE/$projectId",
    navOptions = navOptions
)

fun NavGraphBuilder.computationResultsScreen() {
    composable(
        route = "$COMPUTATION_RESULTS_ROUTE/{$PROJECT_ID_ARG}",
        arguments = listOf(navArgument(PROJECT_ID_ARG) { type = NavType.LongType })
    ) {
        ComputationResultsScreen()
    }
}