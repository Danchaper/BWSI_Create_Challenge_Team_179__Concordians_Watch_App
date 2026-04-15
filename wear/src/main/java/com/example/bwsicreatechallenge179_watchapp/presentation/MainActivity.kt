@file:Suppress("DEPRECATION")

package com.example.bwsicreatechallenge179_watchapp.presentation

import android.app.NotificationManager
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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CompactButton
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

@Composable
fun WatchButtons(
    isRunning: Boolean,
    onToggle: () -> Unit,
    onAddMinute: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    var isDnd by remember { mutableStateOf(notificationManager.currentInterruptionFilter == NotificationManager.INTERRUPTION_FILTER_NONE) }

    Box(contentAlignment = Alignment.Center) {
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
        CompactButton(
            onClick = onAddMinute,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 94.dp)
        ) {
            Text("+1m", fontSize = 8.sp)
        }
        CompactButton(
            onClick = {
                isDnd = !isDnd
                try {
                    notificationManager.setInterruptionFilter(
                        if (isDnd) NotificationManager.INTERRUPTION_FILTER_NONE
                        else NotificationManager.INTERRUPTION_FILTER_ALL
                    )
                } catch (e: Exception) {
                    // silently fail on emulator
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDnd) Color.Red else Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(end = 64.dp)
        ) {
            Icon(
                imageVector = if (isDnd) Icons.Default.NotificationsOff else Icons.Default.Notifications,
                contentDescription = if (isDnd) "DND on" else "DND off",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun TimeRow(time: Int, isSelected: Boolean) {
    Text(
        text = time.toString().padStart(2, '0'),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        textAlign = TextAlign.Center,
        color = if (isSelected) Color.White else Color.Gray,
        style = if (isSelected)
            MaterialTheme.typography.titleMedium
        else
            MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun TaskTextField(state: TextFieldState) {
    val focusManager = LocalFocusManager.current
    TextField(
        textStyle = TextStyle(
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
        ),
        state = state,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = KeyboardActionHandler { focusManager.clearFocus() },
        label = {
            Text(
                text = "",
                modifier = Modifier.padding(vertical = 6.dp),
                fontSize = 10.sp,
                color = Color.White
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
    val paddingCount = 1
    val listState = rememberLazyListState()
    var userHasScrolled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        listState.scrollToItem(paddingCount + (selected - range.first))
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { scrolling ->
                if (scrolling) userHasScrolled = true
            }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collectLatest { layoutInfo ->
                if (!userHasScrolled) return@collectLatest
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
        items(paddingCount) {
            Box(modifier = Modifier.fillMaxWidth().height(40.dp))
        }
        items(items.size) { idx ->
            TimeRow(time = items[idx], isSelected = items[idx] == selected)
        }
        items(paddingCount) {
            Box(modifier = Modifier.fillMaxWidth().height(40.dp))
        }
    }
}

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
        val taskState = rememberTextFieldState(initialText = "What are you working on?")

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
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 300, 200, 300, 200, 300),
                        -1
                    )
                )

                val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)
            }
        }

        if (!showPicker) { TaskTextField(state = taskState) }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val radius = maxWidth.coerceAtMost(maxHeight) / 2f

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "%02d:%02d:%02d".format(hrs, mins, secs),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
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

                if (!showPicker) {
                    WatchButtons(
                        isRunning = isRunning,
                        onToggle = { isRunning = !isRunning },
                        onAddMinute = {
                            if (mins < 59) mins++
                            else if (hrs < 23) { hrs++; mins = 0 }
                        },
                        modifier = Modifier.size(radius * 0.4f)
                    )
                }
            }
        }
    }
}