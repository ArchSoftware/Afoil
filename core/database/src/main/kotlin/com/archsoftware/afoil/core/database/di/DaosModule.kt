package com.archsoftware.afoil.core.database.di

import com.archsoftware.afoil.core.database.AfoilDatabase
import com.archsoftware.afoil.core.database.dao.AfoilProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    internal fun provideAfoilProjectDao(database: AfoilDatabase): AfoilProjectDao {
        return database.afoilProjectDao()
    }
}