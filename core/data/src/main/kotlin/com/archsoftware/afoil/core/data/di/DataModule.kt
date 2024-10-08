package com.archsoftware.afoil.core.data.di

import com.archsoftware.afoil.core.data.repository.AfoilProjectDataRepository
import com.archsoftware.afoil.core.data.repository.AfoilProjectNumResultRepository
import com.archsoftware.afoil.core.data.repository.AfoilProjectPostResultRepository
import com.archsoftware.afoil.core.data.repository.AfoilProjectRepository
import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import com.archsoftware.afoil.core.data.repository.ProjectDataRepository
import com.archsoftware.afoil.core.data.repository.ProjectNumResultRepository
import com.archsoftware.afoil.core.data.repository.ProjectPostResultRepository
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
    fun bindsProjectDataRepository(
        projectDataRepository: AfoilProjectDataRepository
    ): ProjectDataRepository

    @Binds
    fun bindsProjectNumResultRepository(
        projectNumResultRepository: AfoilProjectNumResultRepository
    ): ProjectNumResultRepository

    @Binds
    fun bindsProjectPostResultRepository(
        projectPostResultRepository: AfoilProjectPostResultRepository
    ): ProjectPostResultRepository

    @Binds
    fun bindsPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepository
    ): PreferencesRepository
}