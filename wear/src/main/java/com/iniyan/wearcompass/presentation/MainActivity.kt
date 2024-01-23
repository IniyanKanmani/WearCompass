/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.iniyan.wearcompass.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.iniyan.wearcompass.Compass
import com.iniyan.wearcompass.DailyMoonPhaseWorker
import com.iniyan.wearcompass.CompassDataForegroundService
import com.iniyan.wearcompass.R
import com.iniyan.wearcompass.SOTWFormatter
import com.iniyan.wearcompass.presentation.theme.WearCompassTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private companion object {
        const val TAG = "WearCompassMainActivity"
    }

    private lateinit var compass: Compass

    private lateinit var sotwFormatter: SOTWFormatter

    private lateinit var foregroundIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreate", "I am Called")

        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        sotwFormatter = SOTWFormatter(this)
        setupCompass()

        foregroundIntent = Intent(this, CompassDataForegroundService::class.java)

        scheduleDailyWork(applicationContext)

        setContent {
            WearApp("Android")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "start compass")
        compass.start()
//        Thread {
//            // Perform network operations here
//            val moonPhaseData: MoonPhaseData = MoonPhaseData()
//            moonPhaseData.getMoonPhaseInfo()
//            runOnUiThread {
//                // Update UI with the result
//            }
//        }.start()
    }

    override fun onPause() {
        super.onPause()
        compass.stop()
//        this.startForegroundService(foregroundIntent)
    }

    override fun onResume() {
        super.onResume()
//        this.stopService(foregroundIntent)
        compass.start()
    }

    override fun onStop() {
        Log.d(TAG, "stop compass")
        compass.stop()
//        this.startForegroundService(foregroundIntent)
        super.onStop()
    }

    private fun setupCompass() {
        compass = Compass(this)
        compass.setListener(getCompassListener())
    }

    private fun adjustSotwLabel(azimuth: Float) {
        Log.d(TAG, sotwFormatter.format(azimuth))
        setContent {
            WearApp(sotwFormatter.format(azimuth))
        }
    }

    private fun scheduleDailyWork(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<DailyMoonPhaseWorker>(24, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("daily_moon_phase_data_update_task",
            ExistingPeriodicWorkPolicy.KEEP, workRequest)

    }

    private fun getCompassListener() = object : Compass.CompassListener {
        override fun onNewAzimuth(azimuth: Float) {
            runOnUiThread {
                adjustSotwLabel(azimuth)
            }
        }
    }

    override fun onDestroy() {
//        compass.stop()
//        this.startForegroundService(foregroundIntent)
        super.onDestroy()
    }

}

@Composable
fun WearApp(greetingName: String) {
    WearCompassTheme {
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

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}
