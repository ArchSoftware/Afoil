package com.archsoftware.afoil.core.model

/**
 * External data layer representation of Afoil project data.
 */
data class AfoilProjectData(
    val id: Long = 0,
    val uri: String,
    val projectOwnerId: Long
)