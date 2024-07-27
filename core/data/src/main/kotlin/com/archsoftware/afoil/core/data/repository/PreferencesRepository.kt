package com.archsoftware.afoil.core.data.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getAfoilProjectsDirectory(): Flow<String?>
    suspend fun updateAfoilProjectsDirectory(projectsDir: String)
}