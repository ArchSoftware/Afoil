package com.archsoftware.afoil.core.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.archsoftware.afoil.core.designsystem.R
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme

/**
 * Afoil [TopAppBar] with a selection action bar. It switches between a normal [TopAppBar] and
 * a selection action bar depending on the value of [showActionBar].
 *
 * @param title The title to display.
 * @param navIcon The navigation icon to display.
 * @param navIconContentDescription The content description for the navigation icon.
 * @param selectedCount The number of selected items.
 * @param showActionBar Whether to show the selection action bar.
 * @param onNavIconClick The action to perform when the navigation icon is clicked.
 * @param onCancelIconClick The action to perform when the cancel icon is clicked.
 * @param modifier The modifier to be applied to this app bar.
 * @param actions The actions to display in the selection action bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionAppBar(
    title: String,
    navIcon: ImageVector,
    navIconContentDescription: String,
    selectedCount: Int,
    showActionBar: Boolean,
    onNavIconClick: () -> Unit,
    onCancelIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit
) {
    AnimatedContent(
        targetState = showActionBar,
        modifier = modifier,
        label = "selectionAppBarAnimation"
    ) { targetState ->
        if (!targetState) {
            NavCenterAlignedAppBar(
                title = title,
                navIcon = navIcon,
                navIconContentDescription = navIconContentDescription,
                onNavIconClick = onNavIconClick
            )
        } else {
            TopAppBar(
                title = { Text(text = selectedCount.toString()) },
                navigationIcon = {
                    IconButton(onClick = onCancelIconClick) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(
                                id = R.string.core_designsystem_selectionappbar_cancel_selection
                            )
                        )
                    }
                },
                actions = actions
            )
        }
    }
}

@Preview
@Composable
fun SelectionAppBarPreview() {
    AfoilTheme {
        SelectionAppBar(
            title = "Title",
            navIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navIconContentDescription = "",
            selectedCount = 0,
            showActionBar = false,
            onNavIconClick = {},
            onCancelIconClick = {},
            actions = {}
        )
    }
}