package com.iniyan.wearcompass

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import com.iniyan.wearcompass.complication.MainComplicationService

class TransferCompassDataBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.hasExtra("message")) {
                MainComplicationService.currentSOTWValue =
                    intent.getStringExtra("message").toString()
            } else {

                Thread {
                    // Perform network operations here
                    val moonPhaseData: MoonPhaseData = MoonPhaseData()
                    moonPhaseData.getMoonPhaseInfo()
                    MainComplicationService.currentSOTWValue =
                        moonPhaseData.moonPhaseInPercentage.toString()
                }.start()
            }

            if (context != null) {
                ComplicationDataSourceUpdateRequester.create(
                    context, ComponentName(context, MainComplicationService::class.java)
                ).requestUpdateAll()
            }
        }
    }
}