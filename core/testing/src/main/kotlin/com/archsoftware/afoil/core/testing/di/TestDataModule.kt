package com.archsoftware.afoil.core.testing.di

import com.archsoftware.afoil.core.data.di.DataModule
import com.archsoftware.afoil.core.data.repository.AnalysisProjectRepository
import com.archsoftware.afoil.core.data.repository.PreferencesRepository
import com.archsoftware.afoil.core.testing.repository.TestAirfoilAnalysisProjectRepository
import com.archsoftware.afoil.core.testing.repository.TestUserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface TestDataModule {
    @Binds
    fun bindsAnalysisProjectRepository(
        projectRepository: TestAirfoilAnalysisProjectRepository
    ): AnalysisProjectRepository

    @Binds
    fun bindsPreferencesRepository(
        userPreferencesRepository: TestUserPreferencesRepository
    ): PreferencesRepository
}