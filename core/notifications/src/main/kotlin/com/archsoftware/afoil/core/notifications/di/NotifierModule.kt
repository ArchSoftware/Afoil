package com.archsoftware.afoil.core.notifications.di

import com.archsoftware.afoil.core.notifications.Notifier
import com.archsoftware.afoil.core.notifications.SystemTrayNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface NotifierModule {
    @Binds
    fun bindsNotifier(notifier: SystemTrayNotifier): Notifier
}