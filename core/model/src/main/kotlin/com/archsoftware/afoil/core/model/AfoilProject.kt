package com.archsoftware.afoil.core.model

/**
 * External data layer representation of Afoil project.
 */
data class AfoilProject(
    val id: Long = 0,
    val name: String,
    val dirUri: String,
    val projectDataType: String
)
