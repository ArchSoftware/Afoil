package com.archsoftware.afoil.feature.airfoilanalysis.page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.ui.PageWrapper
import com.archsoftware.afoil.feature.airfoilanalysis.AirfoilAnalysisPage
import com.archsoftware.afoil.feature.airfoilanalysis.R

@Composable
fun ProjectDetailsPage(
    projectName: String,
    projectNameHasError: Boolean,
    onProjectNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    PageWrapper(
        title = stringResource(id = AirfoilAnalysisPage.PROJECT_DETAILS.titleId),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.feature_airfoilanalysis_projectdetailspage_enter_project_details),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        OutlinedTextField(
            value = projectName,
            label = { Text(text = stringResource(id = R.string.feature_airfoilanalysis_projectdetailspage_project_name)) },
            isError = projectNameHasError,
            supportingText = {
                if (projectNameHasError && projectName.isEmpty()) {
                    Text(text = stringResource(id = R.string.feature_airfoilanalysis_projectdetailspage_empty_project_name_error))
                } else if (projectNameHasError) {
                    Text(text = stringResource(id = R.string.feature_airfoilanalysis_projectdetailspage_duplicate_project_name_error))
                }
            },
            singleLine = true,
            onValueChange = onProjectNameChange,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun ProjectDetailsPagePreview() {
    AfoilTheme {
        ProjectDetailsPage(
            projectName = "",
            projectNameHasError = false,
            onProjectNameChange = {}
        )
    }
}