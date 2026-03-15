package com.lexlock.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lexlock.domain.model.VocabularyCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CategorySection(
                    selectedCategory = settings.selectedCategory,
                    onCategorySelected = { viewModel.updateCategory(it) }
                )
            }

//            item {
//                HorizontalDivider()
//            }

            item {
                NotificationSection(
                    enabled = settings.notificationsEnabled,
                    onToggle = { viewModel.toggleNotifications(it) }
                )
            }

//            item {
//                HorizontalDivider()
//            }

            item {
                ThemeSection(
                    selectedTheme = settings.theme,
                    onThemeSelected = { viewModel.updateTheme(it) }
                )
            }
        }
    }
}

@Composable
fun CategorySection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column {
        Text(text = "Vocabulary Category", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        VocabularyCategory.values().forEach { category ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (category.title == selectedCategory),
                    onClick = { onCategorySelected(category.title) }
                )
                Text(text = category.title)
            }
        }
    }
}

@Composable
fun NotificationSection(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Daily Notifications", style = MaterialTheme.typography.titleMedium)
        Switch(checked = enabled, onCheckedChange = onToggle)
    }
}

@Composable
fun ThemeSection(
    selectedTheme: String,
    onThemeSelected: (String) -> Unit
) {
    Column {
        Text(text = "Wallpaper Theme", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        listOf("Dark", "Amoled", "Nature", "Ocean").forEach { theme ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (theme == selectedTheme),
                    onClick = { onThemeSelected(theme) }
                )
                Text(text = theme)
            }
        }
    }
}
