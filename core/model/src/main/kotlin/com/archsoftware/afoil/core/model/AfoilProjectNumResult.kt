package com.archsoftware.afoil.core.model

/**
 * External data layer representation of Afoil project numerical result.
 */
data class AfoilProjectNumResult(
    val id: Long = 0,
    val uri: String,
    val projectOwnerId: Long
)