package com.archsoftware.afoil.core.database.di

import com.archsoftware.afoil.core.database.AfoilDatabase
import com.archsoftware.afoil.core.database.dao.AirfoilAnalysisProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    internal fun provideAirfoilAnalysisProjectDao(database: AfoilDatabase): AirfoilAnalysisProjectDao {
        return database.airfoilAnalysisProjectDao()
    }
}