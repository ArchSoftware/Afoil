package com.archsoftware.afoil

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.requestFocus
import androidx.core.app.ActivityOptionsCompat
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import com.archsoftware.afoil.core.testing.util.onNodeWithStringId
import com.archsoftware.afoil.feature.airfoilanalysis.R
import com.archsoftware.afoil.feature.airfoilanalysis.page.AirfoilDataPage
import org.junit.Rule
import kotlin.test.Test

class AirfoilDataPageTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val expectedResult = TestAfoilContentResolver().testUri
    private val testRegistryOwner = object : ActivityResultRegistryOwner {
        override val activityResultRegistry = object : ActivityResultRegistry() {
            override fun <I, O> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                dispatchResult(requestCode, expectedResult)
            }
        }
    }

    @Test
    fun airfoildatapage_datAirfoilPickerTest() {
        var result: Uri? = null

        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides testRegistryOwner) {
                AirfoilDataPage(
                    datAirfoilName = null,
                    panelsNumber = String(),
                    panelsNumberHasError = false,
                    onDatAirfoilSelected = { uri -> result = uri },
                    onPanelsNumberChange = {}
                )
            }
        }

        composeTestRule
            .onNodeWithStringId(R.string.feature_airfoilanalysis_airfoildatapage_open_datairfoil)
            .performClick()
        assert(result == expectedResult)
    }

    @Test
    fun airfoildatapage_shouldClearFocusOnSelectDatAirfoil() {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides testRegistryOwner) {
                AirfoilDataPage(
                    datAirfoilName = null,
                    panelsNumber = String(),
                    panelsNumberHasError = false,
                    onDatAirfoilSelected = {},
                    onPanelsNumberChange = {}
                )
            }
        }

        composeTestRule
            .onNodeWithStringId(R.string.feature_airfoilanalysis_airfoildatapage_panels_number)
            .requestFocus()
        composeTestRule
            .onNodeWithStringId(R.string.feature_airfoilanalysis_airfoildatapage_open_datairfoil)
            .performClick()
        composeTestRule
            .onNodeWithStringId(R.string.feature_airfoilanalysis_airfoildatapage_panels_number)
            .assertIsNotFocused()
    }
}