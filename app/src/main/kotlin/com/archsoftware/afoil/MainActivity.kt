package com.archsoftware.afoil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.ui.AfoilApp
import com.archsoftware.afoil.ui.rememberAfoilAppState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val appState = rememberAfoilAppState()

            AfoilTheme {
                AfoilApp(afoilAppState = appState)
            }
        }
    }
}