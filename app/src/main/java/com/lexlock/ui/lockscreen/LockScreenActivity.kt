package com.lexlock.ui.lockscreen

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.lexlock.domain.model.Word
import com.lexlock.ui.theme.LexLockTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class LockScreenActivity : ComponentActivity() {

    private val viewModel: LockScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set flags to show over lock screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }

        setContent {
            LexLockTheme {
                LockScreenContent(
                    viewModel = viewModel,
                    onUnlock = { finish() }
                )
            }
        }
    }
}

@Composable
fun LockScreenContent(
    viewModel: LockScreenViewModel,
    onUnlock: () -> Unit
) {
    val wordState by viewModel.wordOfTheDay.collectAsState()
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                )
            )
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    offsetY += delta
                    if (offsetY < -300f) {
                        onUnlock()
                    }
                },
                onDragStopped = {
                    offsetY = 0f
                }
            )
            .offset { IntOffset(0, offsetY.roundToInt()) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            wordState?.let { word ->
                Text(
                    text = word.word,
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = word.pronunciation,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = word.meaning,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "\"${word.example}\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
            } ?: CircularProgressIndicator(color = Color.White)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Swipe up to unlock",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}
