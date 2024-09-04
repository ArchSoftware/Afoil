package com.archsoftware.afoil.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.archsoftware.afoil.core.model.AfoilProjectNumResult

internal const val AFOIL_PROJECT_NUM_RESULTS_TABLE_NAME = "afoil_project_num_results"

/**
 * Internal data layer representation of Afoil project numerical result.
 */
@Entity(tableName = AFOIL_PROJECT_NUM_RESULTS_TABLE_NAME)
data class AfoilProjectNumResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val projectOwnerId: Long
)

fun AfoilProjectNumResultEntity.asExternalModel(): AfoilProjectNumResult =
    AfoilProjectNumResult(
        id = id,
        uri = uri,
        projectOwnerId = projectOwnerId
    )