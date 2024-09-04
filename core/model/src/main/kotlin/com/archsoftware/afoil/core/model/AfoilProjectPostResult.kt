package com.archsoftware.afoil.core.model

/**
 * External data layer representation of Afoil project post-processing result.
 */
data class AfoilProjectPostResult(
    val id: Long = 0,
    val nameId: Int,
    val uri: String,
    val projectOwnerId: Long
)
