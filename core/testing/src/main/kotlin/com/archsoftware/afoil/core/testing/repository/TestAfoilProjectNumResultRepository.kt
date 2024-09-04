package com.archsoftware.afoil.core.testing.repository

import com.archsoftware.afoil.core.data.repository.ProjectNumResultRepository
import com.archsoftware.afoil.core.model.AfoilProjectNumResult
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.TestOnly

/**
 * Test-only implementation of the [ProjectNumResultRepository] interface.
 */
@TestOnly
class TestAfoilProjectNumResultRepository : ProjectNumResultRepository {
    private val projectNumResultFlow: MutableSharedFlow<List<AfoilProjectNumResult>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getProjectNumResultByProjectId(projectId: Long): Flow<AfoilProjectNumResult> =
        projectNumResultFlow.map { projectNumResult -> projectNumResult.first { it.projectOwnerId == projectId } }

    override suspend fun insertProjectNumResult(projectNumResult: AfoilProjectNumResult) {}

    override suspend fun deleteProjectNumResultByProjectId(projectId: Long) {}
}