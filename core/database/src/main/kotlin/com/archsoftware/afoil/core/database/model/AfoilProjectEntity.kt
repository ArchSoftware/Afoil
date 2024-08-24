package com.archsoftware.afoil.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.archsoftware.afoil.core.model.AfoilProject

@Entity(tableName = "afoil_projects")
data class AfoilProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val projectDataType: String
)

fun AfoilProjectEntity.asExternalModel(): AfoilProject =
    AfoilProject(
        id = id,
        name = name,
        projectDataType = projectDataType
    )
