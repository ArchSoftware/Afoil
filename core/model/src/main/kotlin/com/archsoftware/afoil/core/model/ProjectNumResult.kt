package com.archsoftware.afoil.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class ProjectNumResult {
    companion object {
        @Transient
        val mimeType: String = "application/json"
        @Transient
        val displayName: String = "projectNumResult.json"
    }
}

@Serializable
data class AirfoilAnalysisNumResult(
    val gammas: List<Double>,
    val psi0: Double,
    val cl: Double,
    val cd: Double,
    val cm: Double
) : ProjectNumResult()