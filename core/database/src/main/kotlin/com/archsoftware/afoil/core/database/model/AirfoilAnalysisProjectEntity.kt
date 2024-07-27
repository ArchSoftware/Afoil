package com.archsoftware.afoil.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.archsoftware.afoil.core.model.AirfoilAnalysisProject

@Entity(tableName = "airfoil_analysis_projects")
data class AirfoilAnalysisProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

fun AirfoilAnalysisProjectEntity.asExternalModel(): AirfoilAnalysisProject =
    AirfoilAnalysisProject(
        name = name
    )
