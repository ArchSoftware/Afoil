package com.archsoftware.afoil.core.testing.di

import com.archsoftware.afoil.core.common.contentresolver.UriContentResolver
import com.archsoftware.afoil.core.common.di.UriContentResolverModule
import com.archsoftware.afoil.core.testing.contentresolver.TestAfoilContentResolver
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UriContentResolverModule::class]
)
internal interface TestUriContentResolverModule {
    @Binds
    fun bindsUriContentResolver(afoilContentResolver: TestAfoilContentResolver): UriContentResolver
}