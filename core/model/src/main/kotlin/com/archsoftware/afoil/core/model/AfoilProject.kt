package com.archsoftware.afoil.core.model

data class AfoilProject(
    val id: Long = 0,
    val name: String,
    val dirUri: String,
    val projectDataType: String
)
