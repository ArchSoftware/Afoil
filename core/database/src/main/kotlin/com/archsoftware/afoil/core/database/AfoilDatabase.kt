package com.archsoftware.afoil.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.archsoftware.afoil.core.database.dao.AirfoilAnalysisProjectDao
import com.archsoftware.afoil.core.database.model.AirfoilAnalysisProjectEntity

@Database(entities = [AirfoilAnalysisProjectEntity::class], version = 1)
internal abstract class AfoilDatabase : RoomDatabase() {
    abstract fun airfoilAnalysisProjectDao(): AirfoilAnalysisProjectDao
}