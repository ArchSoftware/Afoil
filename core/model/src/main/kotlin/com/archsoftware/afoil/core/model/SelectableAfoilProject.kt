package com.archsoftware.afoil.core.model

/**
 * An [AfoilProject] with the additional information for whether it is selected or not.
 */
data class SelectableAfoilProject(
    val afoilProject: AfoilProject,
    val isSelected: Boolean
)
