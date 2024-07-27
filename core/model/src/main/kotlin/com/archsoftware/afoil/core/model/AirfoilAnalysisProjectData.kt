package com.archsoftware.afoil.core.model

data class AirfoilAnalysisProjectData(
    // Airfoil data
    val datAirfoilUri: String,
    val panelsNumber: Int,
    // Fluid data
    val reynoldsNumber: Double?,
    val machNumber: Double,
    val angleOfAttack: Double,
    // Post-processing settings
    val numberOfStreamlines: Int,
    val streamlinesRefiningLevel: Float,
    val pressureContoursGridSize: Int,
    val pressureContoursRefiningLevel: Float
)
