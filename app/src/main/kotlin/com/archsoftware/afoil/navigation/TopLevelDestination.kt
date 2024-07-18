package com.archsoftware.afoil.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(val icon: ImageVector?, @StringRes val titleId: Int?) {
    HOME(
        icon = null,
        titleId = null
    )
}