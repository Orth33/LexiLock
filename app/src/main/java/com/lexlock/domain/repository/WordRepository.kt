package com.lexlock.domain.repository

import com.lexlock.domain.model.UserSettings
import com.lexlock.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun getAllWords(): Flow<List<Word>>
    fun getWordsByCategory(category: String): Flow<List<Word>>
    suspend fun getWordById(id: Int): Word?
    suspend fun insertWords(words: List<Word>)
    suspend fun getWordCountByCategory(category: String): Int
    
    fun getSavedWords(): Flow<List<Word>>
    suspend fun saveWord(wordId: Int)
    suspend fun deleteSavedWord(wordId: Int)
    
    fun getUserSettings(): Flow<UserSettings>
    suspend fun updateSettings(settings: UserSettings)
}
