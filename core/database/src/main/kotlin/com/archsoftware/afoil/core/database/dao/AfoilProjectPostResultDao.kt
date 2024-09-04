package com.archsoftware.afoil.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.archsoftware.afoil.core.database.model.AFOIL_PROJECT_POST_RESULTS_TABLE_NAME
import com.archsoftware.afoil.core.database.model.AfoilProjectPostResultEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [AfoilProjectPostResultEntity] access.
 */
@Dao
interface AfoilProjectPostResultDao {

    @Query("SELECT * FROM $AFOIL_PROJECT_POST_RESULTS_TABLE_NAME WHERE projectOwnerId = :projectId")
    fun getProjectPostResultsByProjectId(projectId: Long): Flow<List<AfoilProjectPostResultEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProjectPostResults(projectPostResults: List<AfoilProjectPostResultEntity>)

    @Delete
    suspend fun deleteProjectPostResults(projectPostResults: List<AfoilProjectPostResultEntity>)
}