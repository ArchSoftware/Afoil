package com.archsoftware.afoil.core.testing.repository

import com.archsoftware.afoil.core.data.repository.ProjectPostResultRepository
import com.archsoftware.afoil.core.model.AfoilProjectPostResult
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.jetbrains.annotations.TestOnly

/**
 * Test-only implementation of the [ProjectPostResultRepository] interface.
 */
@TestOnly
class TestAfoilProjectPostResultRepository : ProjectPostResultRepository {
    private val projectPostResultsFlow: MutableSharedFlow<List<AfoilProjectPostResult>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getProjectPostResultsByProjectId(projectId: Long): Flow<List<AfoilProjectPostResult>> =
        projectPostResultsFlow

    override suspend fun insertProjectPostResult(projectPostResult: AfoilProjectPostResult) {}

    override suspend fun deleteProjectPostResultsByProjectId(projectId: Long) {}
}