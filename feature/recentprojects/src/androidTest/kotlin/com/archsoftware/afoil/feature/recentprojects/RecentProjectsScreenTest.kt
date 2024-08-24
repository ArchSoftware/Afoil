package com.archsoftware.afoil.feature.recentprojects

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.archsoftware.afoil.core.model.AfoilProject
import com.archsoftware.afoil.core.model.SelectableAfoilProject
import com.archsoftware.afoil.core.testing.util.onNodeWithStringId
import org.junit.Rule
import org.junit.Test

class RecentProjectsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Test data
    private val projects = listOf(
        SelectableAfoilProject(
            AfoilProject(
                name = "Project 1",
                projectDataType = ""
            ),
            isSelected = false
        ),
        SelectableAfoilProject(
            AfoilProject(
                name = "Project 2",
                projectDataType = ""
            ),
            isSelected = false
        )
    )

    @Test
    fun recentProjectsScreen_shouldShowLoadingIndicator() {
        composeTestRule.setContent {
            RecentProjectsScreen(
                isLoading = true,
                selectedCount = 0,
                showActionBar = false,
                recentProjects = emptyList(),
                onProjectClick = {},
                onNavigateUp = {},
                onProjectLongClick = {},
                onCancelIconClick = {},
                onDeleteIconClick = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.feature_recentprojects_loading_text)
            .assertIsDisplayed()
    }

    @Test
    fun recentProjectsScreen_shouldShowProjectsList() {
        composeTestRule.setContent {
            RecentProjectsScreen(
                isLoading = false,
                selectedCount = 0,
                showActionBar = false,
                recentProjects = projects,
                onProjectClick = {},
                onNavigateUp = {},
                onProjectLongClick = {},
                onCancelIconClick = {},
                onDeleteIconClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(projects.first().afoilProject.name)
            .assertIsDisplayed()
    }

    @Test
    fun recentProjectsScreen_shouldShowNoProjectFoundMessageIfProjectsListIsEmpty() {
        composeTestRule.setContent {
            RecentProjectsScreen(
                isLoading = false,
                selectedCount = 0,
                showActionBar = false,
                recentProjects = emptyList(),
                onProjectClick = {},
                onNavigateUp = {},
                onProjectLongClick = {},
                onCancelIconClick = {},
                onDeleteIconClick = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.feature_recentprojects_no_project_found)
            .assertIsDisplayed()
    }
}