package com.archsoftware.afoil.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.archsoftware.afoil.core.database.model.AFOIL_PROJECTS_TABLE_NAME
import com.archsoftware.afoil.core.database.model.AfoilProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AfoilProjectDao {

    @Query("SELECT * FROM $AFOIL_PROJECTS_TABLE_NAME")
    fun getProjects(): Flow<List<AfoilProjectEntity>>

    @Query("SELECT * FROM $AFOIL_PROJECTS_TABLE_NAME WHERE name = :name")
    fun getProjectByName(name: String): Flow<AfoilProjectEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProject(project: AfoilProjectEntity): Long

    @Delete
    suspend fun deleteProjects(projects: List<AfoilProjectEntity>)
}