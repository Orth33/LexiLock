package com.lexlock.data.local.dao

import androidx.room.*
import com.lexlock.data.local.entity.SavedWordEntity
import com.lexlock.data.local.entity.UserSettingsEntity
import com.lexlock.data.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE category = :category")
    fun getWordsByCategory(category: String): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE id = :id")
    suspend fun getWordById(id: Int): WordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    @Query("SELECT COUNT(*) FROM words WHERE category = :category")
    suspend fun getWordCountByCategory(category: String): Int
}

@Dao
interface SavedWordDao {
    @Query("SELECT * FROM saved_words")
    fun getAllSavedWords(): Flow<List<SavedWordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWord(savedWord: SavedWordEntity)

    @Delete
    suspend fun deleteSavedWord(savedWord: SavedWordEntity)
}

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE id = 1")
    fun getUserSettings(): Flow<UserSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSettings(settings: UserSettingsEntity)
}
