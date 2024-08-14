package com.archsoftware.afoil.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.archsoftware.afoil.core.database.dao.AfoilProjectDao
import com.archsoftware.afoil.core.database.model.AfoilProjectEntity

@Database(entities = [AfoilProjectEntity::class], version = 1)
internal abstract class AfoilDatabase : RoomDatabase() {
    abstract fun afoilProjectDao(): AfoilProjectDao
}