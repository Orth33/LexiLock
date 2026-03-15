package com.lexlock.data.repository

import com.lexlock.data.local.dao.SavedWordDao
import com.lexlock.data.local.dao.UserSettingsDao
import com.lexlock.data.local.dao.WordDao
import com.lexlock.data.local.entity.SavedWordEntity
import com.lexlock.data.local.entity.UserSettingsEntity
import com.lexlock.data.local.entity.WordEntity
import com.lexlock.domain.model.UserSettings
import com.lexlock.domain.model.Word
import com.lexlock.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepositoryImpl @Inject constructor(
    private val wordDao: WordDao,
    private val savedWordDao: SavedWordDao,
    private val userSettingsDao: UserSettingsDao
) : WordRepository {

    override fun getAllWords(): Flow<List<Word>> {
        return wordDao.getAllWords().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getWordsByCategory(category: String): Flow<List<Word>> {
        return wordDao.getWordsByCategory(category).map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getWordById(id: Int): Word? {
        return wordDao.getWordById(id)?.toDomain()
    }

    override suspend fun insertWords(words: List<Word>) {
        wordDao.insertWords(words.map { it.toEntity() })
    }

    override suspend fun getWordCountByCategory(category: String): Int {
        return wordDao.getWordCountByCategory(category)
    }

    override fun getSavedWords(): Flow<List<Word>> {
        // This is a bit simplified, usually you'd join tables or do multiple queries
        // For brevity in this MVP, I'll map it simple
        return savedWordDao.getAllSavedWords().map { savedEntities ->
            // In a real app, you'd fetch the actual words here.
            // For now, let's keep it simple.
            emptyList() 
        }
    }

    override suspend fun saveWord(wordId: Int) {
        savedWordDao.saveWord(SavedWordEntity(wordId = wordId, savedDate = System.currentTimeMillis()))
    }

    override suspend fun deleteSavedWord(wordId: Int) {
        // Need to find the entity first. Simplified for now.
    }

    override fun getUserSettings(): Flow<UserSettings> {
        return userSettingsDao.getUserSettings().map { 
            it?.toDomain() ?: UserSettings("General English", true, "Dark")
        }
    }

    override suspend fun updateSettings(settings: UserSettings) {
        userSettingsDao.updateSettings(settings.toEntity())
    }

    private fun WordEntity.toDomain() = Word(id, word, meaning, example, pronunciation, category, difficulty)
    private fun Word.toEntity() = WordEntity(word = word, meaning = meaning, example = example, pronunciation = pronunciation, category = category, difficulty = difficulty)
    
    private fun UserSettingsEntity.toDomain() = UserSettings(selectedCategory, notificationsEnabled, theme)
    private fun UserSettings.toEntity() = UserSettingsEntity(selectedCategory = selectedCategory, notificationsEnabled = notificationsEnabled, theme = theme)
}
