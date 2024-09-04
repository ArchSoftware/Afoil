package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.datastore.UserPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository that provides management methods for user preferences.
 */
class UserPreferencesRepository @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : PreferencesRepository {
    override fun getAfoilProjectsDirectory(): Flow<String?> = dataStore.getAfoilProjectsDirectory()

    override suspend fun updateAfoilProjectsDirectory(projectsDir: String) {
        dataStore.updateAfoilProjectsDirectory(projectsDir)
    }
}