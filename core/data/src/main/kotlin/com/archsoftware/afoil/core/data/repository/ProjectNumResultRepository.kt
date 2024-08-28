package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.model.AfoilProjectNumResult
import kotlinx.coroutines.flow.Flow

interface ProjectNumResultRepository {
    fun getProjectNumResultByProjectId(projectId: Long): Flow<AfoilProjectNumResult>
    suspend fun insertProjectNumResult(projectNumResult: AfoilProjectNumResult)
    suspend fun deleteProjectNumResultByProjectId(projectId: Long)
}