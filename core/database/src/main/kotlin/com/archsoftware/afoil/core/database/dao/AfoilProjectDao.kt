package com.archsoftware.afoil.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.archsoftware.afoil.core.database.model.AfoilProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AfoilProjectDao {

    @Query("SELECT * FROM afoil_projects")
    fun getProjects(): Flow<List<AfoilProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProject(project: AfoilProjectEntity)
}