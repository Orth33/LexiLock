package com.lexlock.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val meaning: String,
    val example: String,
    val pronunciation: String,
    val category: String,
    val difficulty: String
)

@Entity(tableName = "saved_words")
data class SavedWordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wordId: Int,
    val savedDate: Long
)

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1, // Single row for settings
    val selectedCategory: String,
    val notificationsEnabled: Boolean,
    val theme: String
)
