package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.model.AfoilProjectPostResult
import kotlinx.coroutines.flow.Flow

interface ProjectPostResultRepository {
    fun getProjectPostResultsByProjectId(projectId: Long): Flow<List<AfoilProjectPostResult>>
    suspend fun insertProjectPostResult(projectPostResult: AfoilProjectPostResult)
    suspend fun deleteProjectPostResultsByProjectId(projectId: Long)
}