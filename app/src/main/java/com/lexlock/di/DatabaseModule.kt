package com.lexlock.di

import android.content.Context
import androidx.room.Room
import com.lexlock.data.local.LexLockDatabase
import com.lexlock.data.local.dao.SavedWordDao
import com.lexlock.data.local.dao.UserSettingsDao
import com.lexlock.data.local.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LexLockDatabase {
        return Room.databaseBuilder(
            context,
            LexLockDatabase::class.java,
            LexLockDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideWordDao(database: LexLockDatabase): WordDao = database.wordDao()

    @Provides
    fun provideSavedWordDao(database: LexLockDatabase): SavedWordDao = database.savedWordDao()

    @Provides
    fun provideUserSettingsDao(database: LexLockDatabase): UserSettingsDao = database.userSettingsDao()
}
