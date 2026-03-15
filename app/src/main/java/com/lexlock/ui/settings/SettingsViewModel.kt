package com.lexlock.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lexlock.domain.model.UserSettings
import com.lexlock.domain.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {

    val settings: StateFlow<UserSettings> = repository.getUserSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSettings("General English", true, "Dark")
        )

    fun updateCategory(category: String) {
        viewModelScope.launch {
            val current = settings.value
            repository.updateSettings(current.copy(selectedCategory = category))
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value
            repository.updateSettings(current.copy(notificationsEnabled = enabled))
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            val current = settings.value
            repository.updateSettings(current.copy(theme = theme))
        }
    }
}
