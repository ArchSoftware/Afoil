package com.archsoftware.afoil.core.projectstore.di

import com.archsoftware.afoil.core.projectstore.AfoilProjectStore
import com.archsoftware.afoil.core.projectstore.ProjectStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ProjectStoreModule {
    @Binds
    fun bindProjectStore(projectStore: AfoilProjectStore): ProjectStore
}