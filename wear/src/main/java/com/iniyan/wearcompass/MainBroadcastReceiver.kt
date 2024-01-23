package com.iniyan.wearcompass

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startForegroundService

class MainBroadcastReceiver : BroadcastReceiver() {
    private lateinit var foregroundIntent: Intent
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MainBroadcastReceiver", "I am Called")
        foregroundIntent = Intent(context, CompassDataForegroundService::class.java)
        if (intent != null) {

//            if (intent.action == "android.support.wearable.complications.ACTION_COMPLICATION_UPDATE_REQUEST") {
//                if (context != null) {
//                    startForegroundService(context, foregroundIntent)
//                }
//            } else if (intent.action == "com.iniyan.wearcompass.START_COMPASS_DATA") {
//                if (context != null) {
//                    startForegroundService(context, foregroundIntent)
//                }
//
//            } else if (intent.action == "com.iniyan.wearcompass.STOP_COMPASS_DATA") {
//                context?.stopService(foregroundIntent)
//            }


//            if (intent.hasExtra("message")) {
//                MainComplicationService.currentSOTWValue =
//                    intent.getStringExtra("message").toString()
//            }
//            else {
//
//                Thread {
//                    // Perform network operations here
//                    val moonPhaseData: DailyMoonPhaseWorker = DailyMoonPhaseWorker()
//                    moonPhaseData.getMoonPhaseInfo()
//                    MainComplicationService.currentSOTWValue =
//                        moonPhaseData.moonPhaseInPercentage.toString()
//                }.start()
//            }

//            if (context != null) {
//                ComplicationDataSourceUpdateRequester.create(
//                    context, ComponentName(context, MainComplicationService::class.java)
//                ).requestUpdateAll()
//            }
        }
    }
}