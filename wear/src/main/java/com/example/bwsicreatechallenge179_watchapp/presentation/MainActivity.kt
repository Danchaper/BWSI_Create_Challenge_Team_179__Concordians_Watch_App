package com.example.bwsicreatechallenge179_watchapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material3.IconButton
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.bwsicreatechallenge179_watchapp.R
import com.example.bwsicreatechallenge179_watchapp.presentation.theme.BWSICreateChallenge179WatchAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            TimerApp()
        }
    }
}

//@Composable
//fun WearApp(greetingName: String) {
//    BWSICreateChallenge179WatchAppTheme {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colors.background),
//            contentAlignment = Alignment.Center
//        ) {
//            TimeText()
//        }
//    }
//}

@Composable
fun ToggleIconButtonExample(modifier: Modifier = Modifier) {
    var isToggled by rememberSaveable { mutableStateOf(false) }

    IconButton(
        modifier = modifier,
        onClick = { isToggled = !isToggled }
    ) {
        if (!isToggled) {
            Icon(
                painter = painterResource(R.drawable.play_button),
                contentDescription = "Play button",
                tint = Color.Unspecified
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.pause_button),
                contentDescription = "Play button",
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun TimerText(modifier: Modifier = Modifier) {
    val hrs = 20
    val mins = 3
    val secs = 0

    val currentTimeText = "%02d:%02d:%02d".format(hrs, mins, secs)

    Text(
        text = currentTimeText,
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TimerApp() {
    BWSICreateChallenge179WatchAppTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            // Determine safe radius for the round screen
            val radius = maxWidth.coerceAtMost(maxHeight) / 2f

            // Center timer text
            TimerText(
                modifier = Modifier
                    .align(Alignment.Center)
//                    .size(size = 120.dp)
            )

            // Bottom-center button, fully visible
            val buttonSize = radius * 0.4f // 40% of radius
            val safeBottomPadding = radius * 0.15f // move it up from bottom curve

            ToggleIconButtonExample(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = safeBottomPadding)
                    .size(buttonSize)
            )
        }
    }
}

