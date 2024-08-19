package com.archsoftware.afoil.core.notifications.di

import com.archsoftware.afoil.core.notifications.Notifier
import com.archsoftware.afoil.core.notifications.SystemTrayNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface NotifierModule {
    @Binds
    @Singleton
    fun bindsNotifier(notifier: SystemTrayNotifier): Notifier
}