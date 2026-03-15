package com.lexlock.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lexlock.data.local.dao.SavedWordDao
import com.lexlock.data.local.dao.UserSettingsDao
import com.lexlock.data.local.dao.WordDao
import com.lexlock.data.local.entity.SavedWordEntity
import com.lexlock.data.local.entity.UserSettingsEntity
import com.lexlock.data.local.entity.WordEntity

@Database(entities = [WordEntity::class, SavedWordEntity::class, UserSettingsEntity::class], version = 1, exportSchema = false)
abstract class LexLockDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun savedWordDao(): SavedWordDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        const val DATABASE_NAME = "lexlock_db"
    }
}
