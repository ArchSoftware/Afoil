package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.model.AirfoilAnalysisProject
import kotlinx.coroutines.flow.Flow

interface AnalysisProjectRepository {
    fun getProjects(): Flow<List<AirfoilAnalysisProject>>
    suspend fun insertProject(project: AirfoilAnalysisProject)
}