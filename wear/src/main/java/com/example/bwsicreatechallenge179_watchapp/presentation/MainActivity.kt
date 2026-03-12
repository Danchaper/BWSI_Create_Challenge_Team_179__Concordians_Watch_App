/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.bwsicreatechallenge179_watchapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material3.FilledIconButton
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.bwsicreatechallenge179_watchapp.R
import com.example.bwsicreatechallenge179_watchapp.presentation.theme.BWSICreateChallenge179WatchAppTheme
import android.R.attr
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material3.IconButton

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp("Android")
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    BWSICreateChallenge179WatchAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TimerText() {
    BWSICreateChallenge179WatchAppTheme {
        var hrs = 20
        var mins = 3
        var secs = 0
//        var timeInts = arrayOf(hrs, mins, secs)
        var hrsText = "00"
        var minsText = "00"
        var secsText = "00"
//        var timeStrs = arrayOf(hrsText, minsText, secsText)
        var currentTime = mutableMapOf(hrs to hrsText, mins to minsText, secs to secsText)
        for (key in currentTime.keys) {
            if (key < 10) {
                currentTime[key] = "0$key"
            } else {
                currentTime[key] = "$key"
            }
        }
        var currentTimeText = "${hrsText}:${minsText}:${secsText}"

        currentTimeText = "${hrsText}:${minsText}:${secsText}"
    Box (modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Text(currentTimeText)
        }
    }
        }
}
@Composable
fun ToggleIconButtonExample() {
    // isToggled initial value should be read from a view model or persistent storage.
    var isToggled by rememberSaveable { mutableStateOf(false) }

    IconButton(
        onClick = { isToggled = !isToggled }
    ) {
        Icon(
            painter = if (isToggled) painterResource(R.drawable.favorite_filled) else painterResource(R.drawable.favorite),
            contentDescription = if (isToggled) "Selected icon button" else "Unselected icon button."
        )
    }
}



@Composable
fun DefaultPreview() {
    WearApp("dan")
}