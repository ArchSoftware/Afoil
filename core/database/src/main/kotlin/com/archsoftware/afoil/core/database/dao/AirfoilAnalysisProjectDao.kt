package com.archsoftware.afoil.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.archsoftware.afoil.core.database.model.AirfoilAnalysisProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AirfoilAnalysisProjectDao {

    @Query("SELECT * FROM airfoil_analysis_projects")
    fun getProjects(): Flow<List<AirfoilAnalysisProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProject(project: AirfoilAnalysisProjectEntity)
}