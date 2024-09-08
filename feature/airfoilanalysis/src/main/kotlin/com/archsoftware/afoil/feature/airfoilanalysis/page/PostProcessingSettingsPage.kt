package com.archsoftware.afoil.feature.airfoilanalysis.page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.designsystem.component.NumericTextField
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.ui.PageWrapper
import com.archsoftware.afoil.core.ui.AccuracySlider
import com.archsoftware.afoil.feature.airfoilanalysis.AirfoilAnalysisPage
import com.archsoftware.afoil.feature.airfoilanalysis.R

@Composable
fun PostProcessingSettingsPage(
    numberOfStreamlines: String,
    streamlinesRefinementLevel: Float,
    pressureContoursGridSize: String,
    pressureContoursRefinementLevel: Float,
    numberOfStreamlinesHasError: Boolean,
    pressureContoursGridSizeHasError: Boolean,
    onNumberOfStreamlinesChange: (String) -> Unit,
    onStreamlinesRefinementLevelChange: (Float) -> Unit,
    onPressureContoursGridSizeChange: (String) -> Unit,
    onPressureContoursRefinementLevelChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    PageWrapper(
        title = stringResource(id = AirfoilAnalysisPage.POST_PROCESSING_SETTINGS.titleId),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.feature_airfoilanalysis_postprocessingsettingspage_streamlines),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        NumericTextField(
            value = numberOfStreamlines,
            label = stringResource(id = R.string.feature_airfoilanalysis_postprocessingsettingspage_number_of_streamlines),
            isError = numberOfStreamlinesHasError,
            keyboardType = KeyboardType.Number,
            onValueChange = onNumberOfStreamlinesChange,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        AccuracySlider(
            text = stringResource(id = R.string.feature_airfoilanalysis_postprocessingsettingspage_refinement_level),
            value = streamlinesRefinementLevel,
            supportingText = stringResource(id = R.string.feature_airfoilanalysis_postprocessingsettingspage_refinement_level_supporting_text),
            onValueChange = onStreamlinesRefinementLevelChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.feature_airfoilanalysis_postprocessingsettingspage_pressure_contours),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        NumericTextField(
            value = pressureContoursGridSize,
            label = stringResource(id = R.string.feature_airfoilanalysis_postprocessingsettingspage_grid_size),
            supportingText = stringResource(id = R.string.feature_airfoilanalysis_postprocessingsettingspage_grid_size_supporting_text),
            isError = pressureContoursGridSizeHasError,
            keyboardType = KeyboardType.Number,
            onValueChange = onPressureContoursGridSizeChange,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        AccuracySlider(
            text = stringResource(id = R.string.feature_airfoilanalysis_postprocessingsettingspage_refinement_level),
            value = pressureContoursRefinementLevel,
            supportingText = stringResource(id = R.string.feature_airfoilanalysis_postprocessingsettingspage_refinement_level_supporting_text),
            onValueChange = onPressureContoursRefinementLevelChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun PostProcessingSettingsPagePreview() {
    AfoilTheme {
        PostProcessingSettingsPage(
            numberOfStreamlines = "100",
            streamlinesRefinementLevel = 0.5f,
            pressureContoursGridSize = "100",
            pressureContoursRefinementLevel = 0.5f,
            numberOfStreamlinesHasError = false,
            pressureContoursGridSizeHasError = false,
            onNumberOfStreamlinesChange = {},
            onStreamlinesRefinementLevelChange = {},
            onPressureContoursGridSizeChange = {},
            onPressureContoursRefinementLevelChange = {}
        )
    }
}