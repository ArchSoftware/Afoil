package com.archsoftware.afoil.core.database.di

import com.archsoftware.afoil.core.database.AfoilDatabase
import com.archsoftware.afoil.core.database.dao.AfoilProjectDao
import com.archsoftware.afoil.core.database.dao.AfoilProjectDataDao
import com.archsoftware.afoil.core.database.dao.AfoilProjectNumResultDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    internal fun providesAfoilProjectDao(database: AfoilDatabase): AfoilProjectDao {
        return database.afoilProjectDao()
    }

    @Provides
    internal fun providesAfoilProjectDataDao(database: AfoilDatabase): AfoilProjectDataDao {
        return database.afoilProjectDataDao()
    }

    @Provides
    internal fun providesAfoilProjectNumResultDao(database: AfoilDatabase): AfoilProjectNumResultDao {
        return database.afoilProjectNumResultDao()
    }
}