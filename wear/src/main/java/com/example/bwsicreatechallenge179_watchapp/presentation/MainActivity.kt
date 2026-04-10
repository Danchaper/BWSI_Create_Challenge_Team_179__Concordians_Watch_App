@file:Suppress("DEPRECATION")

package com.example.bwsicreatechallenge179_watchapp.presentation

// ✅ ONLY Material3 imports

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.bwsicreatechallenge179_watchapp.R
import com.example.bwsicreatechallenge179_watchapp.presentation.theme.BWSICreateChallenge179WatchAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent { TimerApp() }
    }
}

// Play/Pause Button
@Composable
fun ToggleIconButton(
    isRunning: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onToggle,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(
                if (!isRunning) R.drawable.play_button else R.drawable.pause_button
            ),
            contentDescription = "toggle",
            tint = Color.Unspecified
        )
    }
}

// Row for Picker
@Composable
fun TimeRow(time: Int, isSelected: Boolean) {
    Text(
        text = "%02d".format(time),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        textAlign = TextAlign.Center,
        color = if (isSelected) Color.White else Color.Gray,
        style = if (isSelected)
            MaterialTheme.typography.titleMedium   // ✅ FIXED
        else
            MaterialTheme.typography.bodyMedium    // ✅ FIXED
    )
}

@Composable
fun TaskTextField() {
    val focusManager = LocalFocusManager.current
    TextField(
        textStyle = TextStyle(
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        ),
        state = rememberTextFieldState(initialText = "What are you working on?"),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        onKeyboardAction = KeyboardActionHandler {
            focusManager.clearFocus()
        },
        label = {
            Text(
                text = "",
                modifier = Modifier.padding(vertical = 6.dp),
                fontSize = 10.sp,
            )
        }
    )
}

@Composable
fun TimePicker(
    range: IntRange,
    selected: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = range.toList()
    val paddingCount = 1  // phantom items above and below
//    val totalItems = paddingCount + items.size + paddingCount
    val listState = rememberLazyListState()

    // Scroll so the selected item starts centered
    LaunchedEffect(Unit) {
        listState.scrollToItem(paddingCount + (selected - range.first))
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collectLatest { layoutInfo ->
                val center = layoutInfo.viewportEndOffset / 2
                val closest = layoutInfo.visibleItemsInfo.minByOrNull {
                    kotlin.math.abs((it.offset + it.size / 2) - center)
                }
                closest?.let { item ->
                    val realIndex = item.index - paddingCount
                    if (realIndex in items.indices) {
                        onSelectedChange(items[realIndex])
                    }
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.height(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top padding phantom items
        items(paddingCount) {
            Box(modifier = Modifier.fillMaxWidth().height(40.dp))
        }
        // Real items
        items(items.size) { idx ->
            TimeRow(time = items[idx], isSelected = items[idx] == selected)
        }
        // Bottom padding phantom items
        items(paddingCount) {
            Box(modifier = Modifier.fillMaxWidth().height(40.dp))
        }
    }
}

// MAIN APP
@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TimerApp() {
    BWSICreateChallenge179WatchAppTheme {
        val context = LocalContext.current
        var hrs by remember { mutableIntStateOf(0) }
        var mins by remember { mutableIntStateOf(0) }
        var secs by remember { mutableIntStateOf(0) }
        var showPicker by remember { mutableStateOf(false) }
        var isRunning by remember { mutableStateOf(false) }

        LaunchedEffect(isRunning) {
            while (isRunning && (hrs > 0 || mins > 0 || secs > 0)) {
                delay(1000)
                when {
                    secs > 0 -> secs--
                    mins > 0 -> { mins--; secs = 59 }
                    hrs > 0 -> { hrs--; mins = 59; secs = 59 }
                }
            }
            if (hrs == 0 && mins == 0 && secs == 0 && isRunning) {
                isRunning = false

                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(
                    VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                )

                val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)
            }
        }
        if (!showPicker) { TaskTextField() }
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val radius = maxWidth.coerceAtMost(maxHeight) / 2f

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "%02d:%02d:%02d".format(hrs, mins, secs),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary, // ✅ FIXED
                    modifier = Modifier.clickable { if (!isRunning) showPicker = !showPicker }
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (showPicker) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TimePicker(0..23, hrs, { hrs = it }, Modifier.weight(1f))
                        TimePicker(0..59, mins, { mins = it }, Modifier.weight(1f))
                        TimePicker(0..59, secs, { secs = it }, Modifier.weight(1f))
                    }
                }
            }

            if (!showPicker) {
                ToggleIconButton(
                    isRunning = isRunning,
                    onToggle = { isRunning = !isRunning },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = radius * 0.15f)
                        .size(radius * 0.4f)
                )
            }
        }
    }
}