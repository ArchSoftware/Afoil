package com.archsoftware.afoil.core.testing.repository

import com.archsoftware.afoil.core.data.repository.ProjectDataRepository
import com.archsoftware.afoil.core.model.AfoilProjectData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.TestOnly

@TestOnly
class TestAfoilProjectDataRepository : ProjectDataRepository {
    private val projectDataFlow: MutableSharedFlow<List<AfoilProjectData>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getProjectDataByProjectId(projectId: Long): Flow<AfoilProjectData> =
        projectDataFlow.map { projectData -> projectData.first { it.projectOwnerId == projectId } }

    override suspend fun insertProjectData(projectData: AfoilProjectData) {}

    override suspend fun deleteProjectDataByProjectId(projectId: Long) {}
}