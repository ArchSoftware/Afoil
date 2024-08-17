package com.archsoftware.afoil.computation.manager.di

import com.archsoftware.afoil.computation.manager.AfoilComputationManager
import com.archsoftware.afoil.computation.manager.ComputationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ComputationManagerModule {
    @Binds
    fun bindsComputationManager(manager: AfoilComputationManager): ComputationManager
}