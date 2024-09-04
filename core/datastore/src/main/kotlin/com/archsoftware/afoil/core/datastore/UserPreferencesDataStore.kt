package com.archsoftware.afoil.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val RECENT_AFOIL_PROJECTS_DIRECTORY = stringPreferencesKey("recent_afoil_projects_directory")

/**
 * Afoil user preferences data store.
 */
class UserPreferencesDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    fun getAfoilProjectsDirectory(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[RECENT_AFOIL_PROJECTS_DIRECTORY]
        }
    }

    suspend fun updateAfoilProjectsDirectory(projectsDir: String) {
        dataStore.edit { preferences ->
            preferences[RECENT_AFOIL_PROJECTS_DIRECTORY] = projectsDir
        }
    }
}