package com.archsoftware.afoil.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.ui.graphics.vector.ImageVector
import com.archsoftware.afoil.feature.airfoilanalysis.R as airfoilanalysisR

enum class TopLevelDestination(val icon: ImageVector?, @StringRes val titleId: Int?) {
    HOME(
        icon = null,
        titleId = null
    ),
    AIRFOIL_ANALYSIS(
        icon = Icons.Filled.QueryStats,
        titleId = airfoilanalysisR.string.feature_airfoilanalysis_title
    )
}