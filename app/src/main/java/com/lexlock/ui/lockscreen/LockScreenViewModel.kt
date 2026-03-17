package com.lexlock.ui.lockscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lexlock.domain.model.Word
import com.lexlock.domain.usecase.GetWordOfTheDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockScreenViewModel @Inject constructor(
    private val getWordOfTheDayUseCase: GetWordOfTheDayUseCase
) : ViewModel() {

    private val _wordOfTheDay = MutableStateFlow<Word?>(null)
    val wordOfTheDay: StateFlow<Word?> = _wordOfTheDay

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        Log.d("LockScreenViewModel", "init")
        fetchWord()
    }

    fun fetchWord() {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("LockScreenViewModel", "Fetching word...")
            
            // Try fetching a few times in case database is still seeding
            var attempts = 0
            var word: Word? = null
            
            while (attempts < 5 && word == null) {
                word = getWordOfTheDayUseCase()
                if (word == null) {
                    Log.d("LockScreenViewModel", "Word is null, retrying in 1s... (Attempt ${attempts + 1})")
                    delay(1000)
                    attempts++
                }
            }
            
            _wordOfTheDay.value = word
            _isLoading.value = false
            Log.d("LockScreenViewModel", "Word fetch complete. Result: ${word?.word ?: "null"}")
        }
    }
}
