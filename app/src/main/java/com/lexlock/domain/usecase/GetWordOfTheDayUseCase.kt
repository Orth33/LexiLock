package com.lexlock.domain.usecase

import com.lexlock.domain.model.Word
import com.lexlock.domain.repository.WordRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

class GetWordOfTheDayUseCase @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(): Word? {
        val settings = repository.getUserSettings().first()
        val categoryWords = repository.getWordsByCategory(settings.selectedCategory).first()
        
        if (categoryWords.isEmpty()) return null
        
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        
        val index = dayOfYear % categoryWords.size
        return categoryWords[index]
    }
}
