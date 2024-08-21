package com.archsoftware.afoil.feature.airfoilanalysis.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.archsoftware.afoil.feature.airfoilanalysis.AirfoilAnalysisScreen

const val AIRFOIL_ANALYSIS_ROUTE = "airfoilanalysis_route"

fun NavController.navigateToAirfoilAnalysis(navOptions: NavOptions? = null) = navigate(
    route = AIRFOIL_ANALYSIS_ROUTE,
    navOptions = navOptions
)

fun NavGraphBuilder.airfoilAnalysisScreen(
    onNavigateUp: () -> Unit,
    onDone: (projectName: String) -> Unit
) {
    composable(route = AIRFOIL_ANALYSIS_ROUTE) {
        AirfoilAnalysisScreen(
            onNavigateUp = onNavigateUp,
            onDone = onDone
        )
    }
}