package com.archsoftware.afoil.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Serializable representation of generic Afoil project data.
 */
@Serializable
sealed class ProjectData {
    companion object {
        @Transient
        val mimeType: String = "application/json"
        @Transient
        val displayName: String = "projectData.json"
    }
}

/**
 * Serializable representation of airfoil analysis project data.
 */
@Serializable
data class AirfoilAnalysisProjectData(
    // Airfoil data
    val datAirfoilDisplayName: String,
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
) : ProjectData()