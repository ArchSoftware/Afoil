package com.archsoftware.afoil.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.archsoftware.afoil.core.database.dao.AfoilProjectDao
import com.archsoftware.afoil.core.database.dao.AfoilProjectDataDao
import com.archsoftware.afoil.core.database.model.AfoilProjectDataEntity
import com.archsoftware.afoil.core.database.model.AfoilProjectEntity

@Database(
    entities = [
        AfoilProjectEntity::class,
        AfoilProjectDataEntity::class,
    ],
    version = 1
)
internal abstract class AfoilDatabase : RoomDatabase() {
    abstract fun afoilProjectDao(): AfoilProjectDao
    abstract fun afoilProjectDataDao(): AfoilProjectDataDao
}