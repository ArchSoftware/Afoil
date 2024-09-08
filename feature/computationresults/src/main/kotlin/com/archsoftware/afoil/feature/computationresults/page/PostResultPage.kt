package com.archsoftware.afoil.feature.computationresults.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.core.model.AfoilProjectPostResult
import com.archsoftware.afoil.core.ui.PageWrapper
import com.archsoftware.afoil.core.common.R as commonR

@Composable
fun PostResultPage(
    postResult: AfoilProjectPostResult,
    modifier: Modifier = Modifier
) {
    PageWrapper(
        title = stringResource(id = postResult.nameId),
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            model = postResult.uri.toUri(),
            loading = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()

                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Loading plot...",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            error = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()

                ) {
                    Text(text = "Error loading plot")
                }
            },
            contentDescription = stringResource(id = postResult.nameId),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun PostResultPagePreview() {
    AfoilTheme {
        PostResultPage(
            postResult = AfoilProjectPostResult(
                id = 0,
                nameId = commonR.string.streamlines_plot,
                uri = "",
                projectOwnerId = 0
            )
        )
    }
}