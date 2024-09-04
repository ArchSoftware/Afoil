package com.archsoftware.afoil.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.archsoftware.afoil.core.model.AfoilProjectData

internal const val AFOIL_PROJECT_DATA_TABLE_NAME = "afoil_project_data"

/**
 * Internal data layer representation of Afoil project data.
 */
@Entity(tableName = AFOIL_PROJECT_DATA_TABLE_NAME)
data class AfoilProjectDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val projectOwnerId: Long
)

fun AfoilProjectDataEntity.asExternalModel(): AfoilProjectData =
    AfoilProjectData(
        id = id,
        uri = uri,
        projectOwnerId = projectOwnerId
    )
