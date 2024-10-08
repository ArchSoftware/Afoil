package com.archsoftware.afoil.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme

/**
 * Afoil [CenterAlignedTopAppBar].
 *
 * @param title The title to display.
 * @param navIcon The navigation icon to display.
 * @param navIconContentDescription The content description for the navigation icon.
 * @param onNavIconClick The action to perform when the navigation icon is clicked.
 * @param modifier The modifier to be applied to this app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavCenterAlignedAppBar(
    title: String,
    navIcon: ImageVector,
    navIconContentDescription: String,
    onNavIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onNavIconClick) {
                Icon(
                    imageVector = navIcon,
                    contentDescription = navIconContentDescription
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun NavCenterAlignedAppBarPreview() {
    AfoilTheme {
        NavCenterAlignedAppBar(
            title = "Title",
            navIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navIconContentDescription = "Back",
            onNavIconClick = {}
        )
    }
}