package com.archsoftware.afoil.feature.airfoilanalysis.page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.designsystem.component.NumericTextField
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.ui.PageWrapper
import com.archsoftware.afoil.feature.airfoilanalysis.AirfoilAnalysisPage
import com.archsoftware.afoil.feature.airfoilanalysis.R

@Composable
fun FluidDataPage(
    reynoldsNumber: String,
    machNumber: String,
    angleOfAttack: String,
    reynoldsNumberHasError: Boolean,
    machNumberHasError: Boolean,
    angleOfAttackHasError: Boolean,
    onReynoldsNumberChange: (String) -> Unit,
    onMachNumberChange: (String) -> Unit,
    onAngleOfAttackChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PageWrapper(
        title = stringResource(id = AirfoilAnalysisPage.FLUID_DATA.titleId),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.feature_airfoilanalysis_input_guidelines),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        NumericTextField(
            value = reynoldsNumber,
            label = stringResource(id = R.string.feature_airfoilanalysis_fluiddatapage_reynolds_number),
            supportingText = stringResource(id = R.string.feature_airfoilanalysis_fluiddatapage_reynolds_number_supporting_text),
            isError = reynoldsNumberHasError,
            onValueChange = onReynoldsNumberChange,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        NumericTextField(
            value = machNumber,
            label = stringResource(id = R.string.feature_airfoilanalysis_fluiddatapage_mach_number),
            isError = machNumberHasError,
            onValueChange = onMachNumberChange,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        NumericTextField(
            value = angleOfAttack,
            label = stringResource(id = R.string.feature_airfoilanalysis_fluiddatapage_angle_of_attack),
            unitOfMeasure = stringResource(id = R.string.feature_airfoilanalysis_fluiddatapage_angle_of_attack_unit_of_measure),
            isError = angleOfAttackHasError,
            onValueChange = onAngleOfAttackChange,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun FluidDataPagePreview() {
    AfoilTheme {
        FluidDataPage(
            reynoldsNumber = "",
            machNumber = "0.3",
            angleOfAttack = "5",
            reynoldsNumberHasError = false,
            machNumberHasError = false,
            angleOfAttackHasError = false,
            onReynoldsNumberChange = {},
            onMachNumberChange = {},
            onAngleOfAttackChange = {}
        )
    }
}