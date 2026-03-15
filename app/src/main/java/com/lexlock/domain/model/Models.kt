package com.lexlock.domain.model

data class Word(
    val id: Int,
    val word: String,
    val meaning: String,
    val example: String,
    val pronunciation: String,
    val category: String,
    val difficulty: String
)

data class UserSettings(
    val selectedCategory: String,
    val notificationsEnabled: Boolean,
    val theme: String
)

enum class VocabularyCategory(val title: String) {
    GENERAL("General English"),
    BUSINESS("Business English"),
    IELTS("IELTS Vocabulary"),
    ACADEMIC("Academic Words"),
    IDIOMS("Idioms / Phrases")
}
