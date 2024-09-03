package com.archsoftware.afoil.core.common.di

import com.archsoftware.afoil.core.common.datairfoilreader.AirfoilReader
import com.archsoftware.afoil.core.common.datairfoilreader.DatAirfoilReader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AirfoilReaderModule {
    @Binds
    fun bindsAirfoilReader(datAirfoilReader: DatAirfoilReader): AirfoilReader
}