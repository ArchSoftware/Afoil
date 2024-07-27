package com.archsoftware.afoil.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.archsoftware.afoil.core.designsystem.util.sharedAxisEnter
import com.archsoftware.afoil.core.designsystem.util.sharedAxisExit
import com.archsoftware.afoil.feature.airfoilanalysis.navigation.airfoilAnalysisScreen
import com.archsoftware.afoil.ui.AfoilAppState
import com.archsoftware.afoil.ui.home.navigation.HOME_ROUTE
import com.archsoftware.afoil.ui.home.navigation.homeScreen

private const val SHARED_AXIS_ANIMATION_DURATION = 300

@Composable
fun AfoilNavHost(
    canNavigate: Boolean,
    appState: AfoilAppState,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
        enterTransition = {
            sharedAxisEnter(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                duration = SHARED_AXIS_ANIMATION_DURATION
            )
        },
        exitTransition = {
            sharedAxisExit(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                duration = SHARED_AXIS_ANIMATION_DURATION / 2
            )
        },
        popEnterTransition = {
            sharedAxisEnter(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                duration = SHARED_AXIS_ANIMATION_DURATION
            )
        },
        popExitTransition = {
            sharedAxisExit(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                duration = SHARED_AXIS_ANIMATION_DURATION / 2
            )
        },
        modifier = modifier
    ) {
        homeScreen(
            canNavigate = canNavigate,
            onDestinationSelected = { destination ->
                appState.navigateToTopLevelDestination(destination)
            }
        )
        airfoilAnalysisScreen(
            onNavigateUp = appState.navController::navigateUp,
            onDone = { /* TODO */ }
        )
    }
}