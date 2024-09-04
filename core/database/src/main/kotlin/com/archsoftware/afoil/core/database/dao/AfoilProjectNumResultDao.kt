package com.archsoftware.afoil.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.archsoftware.afoil.core.database.model.AFOIL_PROJECT_NUM_RESULTS_TABLE_NAME
import com.archsoftware.afoil.core.database.model.AfoilProjectNumResultEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [AfoilProjectNumResultEntity] access.
 */
@Dao
interface AfoilProjectNumResultDao {

    @Query("SELECT * FROM $AFOIL_PROJECT_NUM_RESULTS_TABLE_NAME WHERE projectOwnerId = :projectId")
    fun getProjectNumResultsByProjectId(projectId: Long): Flow<AfoilProjectNumResultEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProjectNumResult(projectNumResult: AfoilProjectNumResultEntity)

    @Query("DELETE FROM $AFOIL_PROJECT_NUM_RESULTS_TABLE_NAME WHERE projectOwnerId = :projectId")
    suspend fun deleteProjectNumResultByProjectId(projectId: Long)
}