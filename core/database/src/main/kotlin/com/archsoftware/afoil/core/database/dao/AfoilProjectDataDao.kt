package com.archsoftware.afoil.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.archsoftware.afoil.core.database.model.AFOIL_PROJECT_DATA_TABLE_NAME
import com.archsoftware.afoil.core.database.model.AfoilProjectDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AfoilProjectDataDao {

    @Query("SELECT * FROM $AFOIL_PROJECT_DATA_TABLE_NAME WHERE projectOwnerId = :projectId")
    fun getProjectDataByProjectId(projectId: Long): Flow<AfoilProjectDataEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProjectData(projectData: AfoilProjectDataEntity)

    @Query("DELETE FROM $AFOIL_PROJECT_DATA_TABLE_NAME WHERE projectOwnerId = :projectId")
    suspend fun deleteProjectDataByProjectId(projectId: Long)
}