package com.archsoftware.afoil.core.data.di

import com.archsoftware.afoil.core.data.repository.AfoilProjectRepository
import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import com.archsoftware.afoil.core.data.repository.ProjectRepository
import com.archsoftware.afoil.core.data.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun bindsProjectRepository(
        projectRepository: AfoilProjectRepository
    ): ProjectRepository

    @Binds
    fun bindsPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepository
    ): PreferencesRepository
}