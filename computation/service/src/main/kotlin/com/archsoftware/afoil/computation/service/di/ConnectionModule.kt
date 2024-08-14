package com.archsoftware.afoil.computation.service.di

import com.archsoftware.afoil.computation.service.connection.ComputationConnection
import com.archsoftware.afoil.computation.service.connection.ComputationServiceConnection
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ConnectionModule {
    @Singleton
    @Binds
    fun bindsComputationServiceConnection(
        computationConnection: ComputationConnection
    ): ComputationServiceConnection
}