package com.archsoftware.afoil.core.testing.repository

import com.archsoftware.afoil.core.data.repository.ProjectRepository
import com.archsoftware.afoil.core.model.AfoilProject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.jetbrains.annotations.TestOnly

@TestOnly
class TestAfoilProjectRepository : ProjectRepository {
    private val projectsFlow: MutableSharedFlow<List<AfoilProject>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getProjects(): Flow<List<AfoilProject>> = projectsFlow

    override suspend fun insertProject(project: AfoilProject) {}

    fun sendProjects(projects: List<AfoilProject>) {
        projectsFlow.tryEmit(projects)
    }
}