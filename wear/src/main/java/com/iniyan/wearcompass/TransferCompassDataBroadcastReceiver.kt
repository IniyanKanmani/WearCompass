package com.iniyan.wearcompass

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import com.iniyan.wearcompass.complication.MainComplicationService

class TransferCompassDataBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            Log.d("MyReceiver", "Received message: ${intent.getStringExtra("message")}")
            MainComplicationService.currentSOTWValue = intent.getStringExtra("message").toString()

            context?.let {
                ComplicationDataSourceUpdateRequester.create(
                    it,
                    ComponentName(context, MainComplicationService::class.java)
                )
            }?.requestUpdate()
        }
    }
}