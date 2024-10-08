package com.archsoftware.afoil.core.testing.repository

import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.jetbrains.annotations.TestOnly

/**
 * Test-only implementation of the [PreferencesRepository] interface.
 */
@TestOnly
class TestUserPreferencesRepository : PreferencesRepository {
    private val afoilProjectsDirectoryFlow: MutableSharedFlow<String?> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getAfoilProjectsDirectory(): Flow<String?> = afoilProjectsDirectoryFlow

    override suspend fun updateAfoilProjectsDirectory(projectsDir: String) {
        afoilProjectsDirectoryFlow.tryEmit(projectsDir)
    }
}