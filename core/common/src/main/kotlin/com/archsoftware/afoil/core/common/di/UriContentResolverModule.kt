package com.archsoftware.afoil.core.common.di

import com.archsoftware.afoil.core.common.contentresolver.AfoilContentResolver
import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface UriContentResolverModule {
    @Binds
    fun bindsUriContentResolver(afoilContentResolver: AfoilContentResolver): UriContentResolver
}