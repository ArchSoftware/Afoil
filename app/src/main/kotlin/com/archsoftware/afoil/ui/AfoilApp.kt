package com.archsoftware.afoil.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.archsoftware.afoil.navigation.AfoilNavHost

@Composable
fun AfoilApp(
    afoilAppState: AfoilAppState,
    modifier: Modifier = Modifier
) {
    AfoilNavHost(
        navController = afoilAppState.navController,
        modifier = modifier
    )
}