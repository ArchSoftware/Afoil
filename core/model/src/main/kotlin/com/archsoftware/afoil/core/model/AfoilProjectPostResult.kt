package com.archsoftware.afoil.core.model

data class AfoilProjectPostResult(
    val id: Long = 0,
    val nameId: Int,
    val uri: String,
    val projectOwnerId: Long
)
