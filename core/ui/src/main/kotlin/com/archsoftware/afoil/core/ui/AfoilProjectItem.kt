package com.archsoftware.afoil.core.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme

/**
 * A composable that displays an Afoil project details.
 *
 * @param name The name of the project.
 * @param icon The icon that identifies the project type.
 * @param isSelected Whether the project is selected.
 * @param onClick The callback to invoke when the item is clicked.
 * @param onLongClick The callback to invoke when the item is long clicked.
 * @param modifier The [Modifier] to apply to this item.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AfoilProjectItem(
    name: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                } else {
                    Color.Unspecified
                }
            )
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = if (!isSelected) {
                icon
            } else {
                Icons.Filled.CheckCircle
            },
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = name)
    }
}

@Preview
@Composable
private fun AfoilProjectItemPreview() {
    AfoilTheme {
        AfoilProjectItem(
            name = "Project name",
            icon = Icons.Filled.QueryStats,
            isSelected = false,
            onClick = {},
            onLongClick = {}
        )
    }
}