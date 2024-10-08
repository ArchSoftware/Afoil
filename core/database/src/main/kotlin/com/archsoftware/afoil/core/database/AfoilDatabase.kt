package com.archsoftware.afoil.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.archsoftware.afoil.core.database.dao.AfoilProjectDao
import com.archsoftware.afoil.core.database.dao.AfoilProjectDataDao
import com.archsoftware.afoil.core.database.dao.AfoilProjectNumResultDao
import com.archsoftware.afoil.core.database.dao.AfoilProjectPostResultDao
import com.archsoftware.afoil.core.database.model.AfoilProjectDataEntity
import com.archsoftware.afoil.core.database.model.AfoilProjectEntity
import com.archsoftware.afoil.core.database.model.AfoilProjectNumResultEntity
import com.archsoftware.afoil.core.database.model.AfoilProjectPostResultEntity

/**
 * [RoomDatabase] implementation for Afoil.
 */
@Database(
    entities = [
        AfoilProjectEntity::class,
        AfoilProjectDataEntity::class,
        AfoilProjectNumResultEntity::class,
        AfoilProjectPostResultEntity::class
    ],
    version = 1
)
internal abstract class AfoilDatabase : RoomDatabase() {
    abstract fun afoilProjectDao(): AfoilProjectDao
    abstract fun afoilProjectDataDao(): AfoilProjectDataDao
    abstract fun afoilProjectNumResultDao(): AfoilProjectNumResultDao
    abstract fun afoilProjectPostResultDao(): AfoilProjectPostResultDao
}