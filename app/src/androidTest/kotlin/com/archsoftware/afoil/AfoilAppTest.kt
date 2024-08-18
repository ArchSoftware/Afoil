package com.archsoftware.afoil

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import com.archsoftware.afoil.core.testing.repository.TestUserPreferencesRepository
import com.archsoftware.afoil.core.testing.util.onNodeWithStringId
import com.archsoftware.afoil.ui.AfoilApp
import com.archsoftware.afoil.ui.rememberAfoilAppState
import org.junit.Rule
import org.junit.Test

class AfoilAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val afoilContentResolver = TestAfoilContentResolver()
    private val userPreferencesRepository = TestUserPreferencesRepository()

    @Test
    fun afoilApp_shouldShowProjectsDirSelectionDialog() {
        composeTestRule.setContent {
            AfoilApp(
                afoilAppState = rememberAfoilAppState(
                    contentResolver = afoilContentResolver,
                    userPreferencesRepository = userPreferencesRepository
                ),
                showNoProjectsDirSelectedMessage = true,
                onSelectProjectsDir = {}
            )
        }

        composeTestRule
            .onNodeWithStringId(R.string.no_projects_dir_selected_message)
            .assertIsDisplayed()
    }
}