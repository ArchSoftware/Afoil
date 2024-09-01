package com.archsoftware.afoil.feature.computationresults.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.model.AirfoilAnalysisNumResult
import com.archsoftware.afoil.core.ui.NumResultRow
import com.archsoftware.afoil.core.ui.PageWrapper
import com.archsoftware.afoil.core.ui.utils.subscript
import com.archsoftware.afoil.feature.computationresults.R

@Composable
fun AirfoilAnalysisNumResultsPage(
    numResult: AirfoilAnalysisNumResult,
    modifier: Modifier = Modifier
) {
    var moreDetailsExpanded by remember { mutableStateOf(false) }
    val arrowOrientation by animateFloatAsState(
        targetValue = if (moreDetailsExpanded) 180f else 0f,
        label = "arrowOrientationAnimation"
    )
    PageWrapper(
        title = stringResource(id = R.string.feature_computationresults_numericalresultspage_title),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.feature_computationresults_numericalresultspage_aero_coef),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        NumResultRow(
            name = stringResource(
                id = R.string.feature_computationresults_numericalresultspage_cl
            ).subscript(
                start = 1,
                end = 2,
                subscriptStyle = MaterialTheme.typography.bodySmall
            ),
            value = numResult.cl,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        NumResultRow(
            name = stringResource(
                id = R.string.feature_computationresults_numericalresultspage_cd
            ).subscript(
                start = 1,
                end = 2,
                subscriptStyle = MaterialTheme.typography.bodySmall
            ),
            value = numResult.cd,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        NumResultRow(
            name = stringResource(
                id = R.string.feature_computationresults_numericalresultspage_cm
            ).subscript(
                start = 1,
                end = 2,
                subscriptStyle = MaterialTheme.typography.bodySmall
            ),
            value = numResult.cm,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.feature_computationresults_numericalresultspage_more_details),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { moreDetailsExpanded = !moreDetailsExpanded }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(
                        id = R.string.feature_computationresults_numericalresultspage_expand_more_content_desc,
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .rotate(arrowOrientation)
                )
            }
        }
        AnimatedVisibility(
            visible = moreDetailsExpanded,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(
                        id = R.string.feature_computationresults_numericalresultspage_body_stream_fun
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                NumResultRow(
                    name = stringResource(
                        id = R.string.feature_computationresults_numericalresultspage_psi0
                    ).subscript(
                        start = 1,
                        end = 2,
                        subscriptStyle = MaterialTheme.typography.bodySmall
                    ),
                    value = numResult.psi0,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.feature_computationresults_numericalresultspage_vort_dist),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = stringResource(
                        id = R.string.feature_computationresults_numericalresultspage_vort_dist_helper_text
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                LazyColumn(
                    contentPadding = PaddingValues(start = 8.dp, top = 8.dp, end = 8.dp)
                ) {
                    itemsIndexed(numResult.gammas) { index, gamma ->
                        NumResultRow(
                            name = stringResource(
                                id = R.string.feature_computationresults_numericalresultspage_gamma_at_node,
                                index
                            ),
                            value = gamma
                        )
                    }
                }
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7)
@Composable
private fun AirfoilAnalysisNumResultsPagePreview() {
    AfoilTheme {
        AirfoilAnalysisNumResultsPage(
            numResult = AirfoilAnalysisNumResult(
                gammas = listOf(
                    -0.001,
                    0.0,
                    0.001,
                    0.002,
                    0.003,
                    0.004,
                    0.005
                ),
                psi0 = 0.0,
                cl = 0.0,
                cd = 0.0,
                cm = 0.0
            )
        )
    }
}