package com.archsoftware.afoil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.archsoftware.afoil.core.common.contentresolver.AfoilContentResolver
import com.archsoftware.afoil.core.data.repository.UserPreferencesRepository
import com.archsoftware.afoil.core.designsystem.theme.AfoilTheme
import com.archsoftware.afoil.ui.AfoilApp
import com.archsoftware.afoil.ui.rememberAfoilAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @Inject
    lateinit var afoilContentResolver: AfoilContentResolver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val appState = rememberAfoilAppState(
                contentResolver = afoilContentResolver,
                userPreferencesRepository = userPreferencesRepository
            )

            AfoilTheme {
                AfoilApp(afoilAppState = appState)
            }
        }
    }
}