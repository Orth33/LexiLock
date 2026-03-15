package com.lexlock.ui.lockscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lexlock.domain.model.Word
import com.lexlock.domain.usecase.GetWordOfTheDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        fetchWord()
    }

    private fun fetchWord() {
        viewModelScope.launch {
            _wordOfTheDay.value = getWordOfTheDayUseCase()
        }
    }
}
