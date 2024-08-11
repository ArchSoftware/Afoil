package com.archsoftware.afoil.core.data.di

import com.archsoftware.afoil.core.data.repository.AirfoilAnalysisProjectRepository
import com.archsoftware.afoil.core.data.repository.AnalysisProjectRepository
import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import com.archsoftware.afoil.core.data.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsAnalysisProjectRepository(
        projectRepository: AirfoilAnalysisProjectRepository
    ): AnalysisProjectRepository

    @Binds
    fun bindsPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepository
    ): PreferencesRepository
}