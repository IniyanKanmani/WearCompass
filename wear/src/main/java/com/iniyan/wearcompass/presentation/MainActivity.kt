/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.iniyan.wearcompass.presentation

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
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
import com.iniyan.wearcompass.Compass
import com.iniyan.wearcompass.R
import com.iniyan.wearcompass.SOTWFormatter
import com.iniyan.wearcompass.presentation.theme.WearCompassTheme

class MainActivity : ComponentActivity() {
    private companion object {
        const val TAG = "CompassActivity"
    }

    private lateinit var compass: Compass
//    private lateinit var arrowView: ImageView
//    private lateinit var sotwLabel: TextView

    private var currentAzimuth = 0f
    private lateinit var sotwFormatter: SOTWFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreate", "I am Called")
        installSplashScreen()

        super.onCreate(savedInstanceState)

        sotwFormatter = SOTWFormatter(this)
//        arrowView = findViewById(R.id.main_image_hands)
//        sotwLabel = findViewById(R.id.sotw_label)
        setupCompass()

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp("Android")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "start compass")
        compass.start()
    }

    override fun onPause() {
        super.onPause()
        compass.stop()
    }

    override fun onResume() {
        super.onResume()
        compass.start()
    }

    override fun onStop() {
        Log.d(TAG, "stop compass")
        compass.stop()
        super.onStop()
    }

    private fun setupCompass() {
        compass = Compass(this)
        compass.setListener(getCompassListener())
    }

    private fun adjustArrow(azimuth: Float) {
        Log.d(TAG, "will set rotation from $currentAzimuth to $azimuth")

        val an = RotateAnimation(
            -currentAzimuth, -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        currentAzimuth = azimuth

        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true

//        arrowView.startAnimation(an)
    }

    private fun adjustSotwLabel(azimuth: Float) {
        Log.d(TAG, sotwFormatter.format(azimuth))
        setContent {
            WearApp(sotwFormatter.format(azimuth))
        }
//        sotwLabel.text = sotwFormatter.format(azimuth)
    }

    private fun getCompassListener() = object : Compass.CompassListener {
        override fun onNewAzimuth(azimuth: Float) {
            runOnUiThread {
                adjustArrow(azimuth)
                adjustSotwLabel(azimuth)
            }
        }
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


//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import android.util.Log
//import android.view.animation.Animation
//import android.view.animation.RotateAnimation
//import android.widget.ImageView
//import android.widget.TextView
//
//class CompassActivity : AppCompatActivity() {
//
//    private companion object {
//        const val TAG = "CompassActivity"
//    }
//
//    private lateinit var compass: Compass
//    private lateinit var arrowView: ImageView
//    private lateinit var sotwLabel: TextView
//
//    private var currentAzimuth = 0f
//    private lateinit var sotwFormatter: SOTWFormatter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_compass)
//
//        sotwFormatter = SOTWFormatter(this)
//
//        arrowView = findViewById(R.id.main_image_hands)
//        sotwLabel = findViewById(R.id.sotw_label)
//        setupCompass()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        Log.d(TAG, "start compass")
//        compass.start()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        compass.stop()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        compass.start()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d(TAG, "stop compass")
//        compass.stop()
//    }
//
//    private fun setupCompass() {
//        compass = Compass(this)
//        compass.setListener(getCompassListener())
//    }
//
//    private fun adjustArrow(azimuth: Float) {
//        Log.d(TAG, "will set rotation from $currentAzimuth to $azimuth")
//
//        val an = RotateAnimation(-currentAzimuth, -azimuth,
//            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
//        currentAzimuth = azimuth
//
//        an.duration = 500
//        an.repeatCount = 0
//        an.fillAfter = true
//
//        arrowView.startAnimation(an)
//    }
//
//    private fun adjustSotwLabel(azimuth: Float) {
//        sotwLabel.text = sotwFormatter.format(azimuth)
//    }
//
//    private fun getCompassListener() = object : Compass.CompassListener {
//        override fun onNewAzimuth(azimuth: Float) {
//            runOnUiThread {
//                adjustArrow(azimuth)
//                adjustSotwLabel(azimuth)
//            }
//        }
//    }
//}
