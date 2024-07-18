package com.archsoftware.afoil.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.archsoftware.afoil.ui.home.navigation.HOME_ROUTE
import com.archsoftware.afoil.ui.home.navigation.homeScreen

@Composable
fun AfoilNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(onDestinationSelected = {})
    }
}