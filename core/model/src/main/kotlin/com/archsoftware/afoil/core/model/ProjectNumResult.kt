package com.archsoftware.afoil.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Serializable representation of generic Afoil project numerical result.
 */
@Serializable
sealed class ProjectNumResult {
    companion object {
        @Transient
        val mimeType: String = "application/json"
        @Transient
        val displayName: String = "projectNumResult.json"
    }
}

/**
 * Serializable representation of airfoil analysis project numerical result.
 */
@Serializable
data class AirfoilAnalysisNumResult(
    val gammas: List<Double>,
    val psi0: Double,
    val cl: Double,
    val cd: Double,
    val cm: Double
) : ProjectNumResult()