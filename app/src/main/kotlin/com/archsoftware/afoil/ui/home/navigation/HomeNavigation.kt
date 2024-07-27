package com.archsoftware.afoil.ui.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.archsoftware.afoil.navigation.TopLevelDestination
import com.archsoftware.afoil.ui.home.HomeScreen

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) = navigate(
    route = HOME_ROUTE,
    navOptions = navOptions
)

fun NavGraphBuilder.homeScreen(
    canNavigate: Boolean,
    onDestinationSelected: (TopLevelDestination) -> Unit
) {
    composable(route = HOME_ROUTE) {
        HomeScreen(
            canNavigate = canNavigate,
            onDestinationSelected = onDestinationSelected
        )
    }
}