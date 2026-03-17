package com.lexlock.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lexlock.domain.repository.WordRepository
import com.lexlock.services.DailyUpdateWorker
import com.lexlock.services.LockScreenService
import com.lexlock.ui.savedwords.SavedWordsScreen
import com.lexlock.ui.settings.SettingsScreen
import com.lexlock.ui.theme.LexLockTheme
import com.lexlock.utils.AssetLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: WordRepository

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Proceed regardless of POST_NOTIFICATIONS grant for MVP
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")
        
        requestPermissions()
        seedDatabaseIfNeeded()

        // Start services
        val serviceIntent = Intent(this, LockScreenService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        
        DailyUpdateWorker.startPeriodicWork(this)

        setContent {
            LexLockTheme {
                MainScreen()
            }
        }
    }

    private fun seedDatabaseIfNeeded() {
        lifecycleScope.launch {
            try {
                val allWords = repository.getAllWords().first()
                if (allWords.isEmpty()) {
                    Log.d("MainActivity", "Seeding database from JSON...")
                    val words = AssetLoader.loadWordsFromJson(applicationContext, "words_seed.json")
                    repository.insertWords(words)
                    Log.d("MainActivity", "Seeding complete: ${words.size} words added.")
                } else {
                    Log.d("MainActivity", "Database already contains ${allWords.size} words.")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to seed database", e)
            }
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.SavedWords,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Settings.route, Modifier.padding(innerPadding)) {
            composable(Screen.SavedWords.route) { SavedWordsScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object SavedWords : Screen("saved", "Saved", Icons.Default.List)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}
