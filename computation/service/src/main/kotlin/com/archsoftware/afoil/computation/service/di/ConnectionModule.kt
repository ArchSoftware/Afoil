package com.archsoftware.afoil.computation.service.di

import com.archsoftware.afoil.computation.service.connection.ComputationConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {
    @Singleton
    @Provides
    internal fun providesComputationConnection(): ComputationConnection = ComputationConnection()
}