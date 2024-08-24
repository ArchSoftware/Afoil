package com.archsoftware.afoil.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.ui.graphics.vector.ImageVector
import com.archsoftware.afoil.feature.airfoilanalysis.R as airfoilanalysisR
import com.archsoftware.afoil.feature.recentprojects.R as recentprojectsR

enum class TopLevelDestination(val icon: ImageVector?, @StringRes val titleId: Int?) {
    HOME(
        icon = null,
        titleId = null
    ),
    AIRFOIL_ANALYSIS(
        icon = Icons.Filled.QueryStats,
        titleId = airfoilanalysisR.string.feature_airfoilanalysis_title
    ),
    RECENT_PROJECTS(
        icon = Icons.Filled.History,
        titleId = recentprojectsR.string.feature_recentprojects_title
    )
}