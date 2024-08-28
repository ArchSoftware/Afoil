package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.model.AfoilProject
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getProjects(): Flow<List<AfoilProject>>
    fun getProjectById(id: Long): Flow<AfoilProject>
    suspend fun insertProject(project: AfoilProject): Long
    suspend fun deleteProjects(projects: List<AfoilProject>)
}