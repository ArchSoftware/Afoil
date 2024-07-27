package com.archsoftware.afoil.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.R
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.navigation.TopLevelDestination

@Composable
fun HomeScreen(
    canNavigate: Boolean,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.afoil_icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TopLevelDestination.entries.forEach {
                if (it.titleId != null && it.icon != null) {
                    TextButton(
                        enabled = canNavigate,
                        onClick = { onDestinationSelected(it) }
                    ) {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(id = it.titleId),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_7)
@Composable
private fun HomeScreenPreview() {
    AfoilTheme {
        HomeScreen(
            canNavigate = true,
            onDestinationSelected = {}
        )
    }
}