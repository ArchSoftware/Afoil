package com.archsoftware.afoil.feature.airfoilanalysis.page

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.designsystem.component.NumericTextField
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.ui.PageWrapper
import com.archsoftware.afoil.feature.airfoilanalysis.AirfoilAnalysisPage
import com.archsoftware.afoil.feature.airfoilanalysis.R

@Composable
fun AirfoilDataPage(
    datAirfoilName: String?,
    panelsNumber: String,
    panelsNumberHasError: Boolean,
    onDatAirfoilSelected: (Uri?) -> Unit,
    onPanelsNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcherForActivityResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        onDatAirfoilSelected(uri)
    }
    val focusManager = LocalFocusManager.current

    PageWrapper(
        title = stringResource(id = AirfoilAnalysisPage.AIRFOIL_DATA.titleId),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.feature_airfoilanalysis_airfoildatapage_datairfoil_helper_text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = datAirfoilName ?: stringResource(
                    id = R.string.feature_airfoilanalysis_airfoildatapage_datairfoil_placeholder
                ),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f)
            )
            FilledTonalButton(
                onClick = {
                    focusManager.clearFocus()
                    launcherForActivityResult.launch(arrayOf("*/*"))
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.FileUpload,
                    contentDescription = null
                )
                Text(
                    text = stringResource(id = R.string.feature_airfoilanalysis_airfoildatapage_open_datairfoil),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Text(
            text = stringResource(id = R.string.feature_airfoilanalysis_airfoildatapage_paneling_options),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.feature_airfoilanalysis_airfoildatapage_paneling_options_helper_text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        NumericTextField(
            value = panelsNumber,
            label = stringResource(id = R.string.feature_airfoilanalysis_airfoildatapage_panels_number),
            isError = panelsNumberHasError,
            keyboardType = KeyboardType.Number,
            onValueChange = onPanelsNumberChange,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun AirfoilDataPagePreview() {
    AfoilTheme {
        AirfoilDataPage(
            datAirfoilName = "NACA 0012",
            panelsNumber = "100",
            panelsNumberHasError = false,
            onDatAirfoilSelected = {},
            onPanelsNumberChange = {}
        )
    }
}