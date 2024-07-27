package com.archsoftware.afoil.core.database.di

import android.content.Context
import androidx.room.Room
import com.archsoftware.afoil.core.database.AfoilDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    internal fun providesAfoilDatabase(@ApplicationContext context: Context): AfoilDatabase {
        return Room.databaseBuilder(context, AfoilDatabase::class.java, "afoil-database")
            .build()
    }
}