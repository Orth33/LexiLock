package com.lexlock.ui.savedwords

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SavedWordsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Saved Words",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Placeholder for now as the logic is simplified
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "Your saved words will appear here.")
        }
    }
}
