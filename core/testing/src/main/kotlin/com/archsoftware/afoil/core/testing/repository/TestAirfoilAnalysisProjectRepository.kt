package com.archsoftware.afoil.core.testing.repository

import com.archsoftware.afoil.core.data.repository.AnalysisProjectRepository
import com.archsoftware.afoil.core.model.AirfoilAnalysisProject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.jetbrains.annotations.TestOnly

@TestOnly
class TestAirfoilAnalysisProjectRepository : AnalysisProjectRepository {
    private val projectsFlow: MutableSharedFlow<List<AirfoilAnalysisProject>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getProjects(): Flow<List<AirfoilAnalysisProject>> = projectsFlow

    override suspend fun insertProject(project: AirfoilAnalysisProject) {}

    fun sendProjects(projects: List<AirfoilAnalysisProject>) {
        projectsFlow.tryEmit(projects)
    }
}