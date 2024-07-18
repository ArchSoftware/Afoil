package com.archsoftware.afoil.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.archsoftware.afoil.navigation.TopLevelDestination
import com.archsoftware.afoil.ui.home.navigation.navigateToHome

@Composable
fun rememberAfoilAppState(
    navController: NavHostController = rememberNavController()
): AfoilAppState = remember(
    navController
) {
    AfoilAppState(
        navController = navController
    )
}

data class AfoilAppState(val navController: NavHostController) {
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(navOptions)
        }
    }
}
