package com.archsoftware.afoil.feature.recentprojects.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.archsoftware.afoil.feature.recentprojects.RecentProjectsScreen

const val RECENT_PROJECTS_ROUTE = "recentprojects_route"

fun NavController.navigateToRecentProjects(navOptions: NavOptions? = null) = navigate(
    route = RECENT_PROJECTS_ROUTE,
    navOptions = navOptions
)

fun NavGraphBuilder.recentProjectsScreen(
    onNavigateUp: () -> Unit,
    onProjectClick: (projectName: String) -> Unit
) {
    composable(route = RECENT_PROJECTS_ROUTE) {
        RecentProjectsScreen(
            onNavigateUp = onNavigateUp,
            onProjectClick = onProjectClick
        )
    }
}