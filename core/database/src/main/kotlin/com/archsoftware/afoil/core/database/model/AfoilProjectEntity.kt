package com.archsoftware.afoil.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.archsoftware.afoil.core.model.AfoilProject

internal const val AFOIL_PROJECTS_TABLE_NAME = "afoil_projects"

@Entity(tableName = AFOIL_PROJECTS_TABLE_NAME)
data class AfoilProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dirUri: String,
    val projectDataType: String
)

fun AfoilProjectEntity.asExternalModel(): AfoilProject =
    AfoilProject(
        id = id,
        name = name,
        dirUri = dirUri,
        projectDataType = projectDataType
    )
