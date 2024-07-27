package com.archsoftware.afoil

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.archsoftware.afoil.core.testing.util.onNodeWithStringId
import com.archsoftware.afoil.feature.airfoilanalysis.R
import com.archsoftware.afoil.feature.airfoilanalysis.page.ProjectDetailsPage
import org.junit.Rule
import org.junit.Test

class ProjectDetailsPageTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun projectDetailsPage_shouldNotShowProjectNameEmptyErrorIfEmptyFieldIsNotError() {
        composeTestRule.setContent {
            ProjectDetailsPage(
                projectName = "",
                projectNameHasError = false,
                onProjectNameChange = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.feature_airfoilanalysis_projectdetailspage_empty_project_name_error)
            .assertIsNotDisplayed()
    }

    @Test
    fun projectDetailsPage_shouldShowProjectNameEmptyErrorIfEmptyFieldIsError() {
        composeTestRule.setContent {
            ProjectDetailsPage(
                projectName = "",
                projectNameHasError = true,
                onProjectNameChange = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.feature_airfoilanalysis_projectdetailspage_empty_project_name_error)
            .assertIsDisplayed()
    }

    @Test
    fun projectDetailsPage_shouldShowProjectNameDuplicateErrorIfNotEmpty() {
        composeTestRule.setContent {
            ProjectDetailsPage(
                projectName = "My Project",
                projectNameHasError = true,
                onProjectNameChange = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.feature_airfoilanalysis_projectdetailspage_duplicate_project_name_error)
            .assertIsDisplayed()
    }
}