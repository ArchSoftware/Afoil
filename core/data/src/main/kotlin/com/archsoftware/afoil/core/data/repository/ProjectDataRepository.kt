package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.model.AfoilProjectData
import kotlinx.coroutines.flow.Flow

interface ProjectDataRepository {
    fun getProjectDataByProjectId(projectId: Long): Flow<AfoilProjectData>
    suspend fun insertProjectData(projectData: AfoilProjectData)
    suspend fun deleteProjectDataByProjectId(projectId: Long)
}