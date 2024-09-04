package com.archsoftware.afoil.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.archsoftware.afoil.core.model.AfoilProjectPostResult

internal const val AFOIL_PROJECT_POST_RESULTS_TABLE_NAME = "afoil_project_post_results"

/**
 * Internal data layer representation of Afoil project post-processing result.
 */
@Entity(tableName = AFOIL_PROJECT_POST_RESULTS_TABLE_NAME)
data class AfoilProjectPostResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameId: Int,
    val uri: String,
    val projectOwnerId: Long
)

fun AfoilProjectPostResultEntity.asExternalModel(): AfoilProjectPostResult =
    AfoilProjectPostResult(
        id = id,
        nameId = nameId,
        uri = uri,
        projectOwnerId = projectOwnerId
    )