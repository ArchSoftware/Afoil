package com.archsoftware.afoil.core.notifications.di

import android.content.Context
import com.archsoftware.afoil.core.notifications.SystemTrayNotifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotifierModule {
    @Singleton
    @Provides
    internal fun providesSystemTrayNotifier(
        @ApplicationContext context: Context
    ): SystemTrayNotifier = SystemTrayNotifier(
        context = context
    )
}