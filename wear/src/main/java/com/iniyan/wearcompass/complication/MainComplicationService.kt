package com.iniyan.wearcompass.complication

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceService
import androidx.wear.watchface.complications.datasource.ComplicationRequest

/**
 * Skeleton for complication data source that returns short text.
 */
class MainComplicationService : ComplicationDataSourceService() {

    companion object {
        const val TAG = "WearCompass"
        var currentSOTWValue = ""
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStart")
        return super.onStartCommand(intent, flags, startId)
    }


    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        Log.d(TAG, "getPreviewData")
        if (type != ComplicationType.SHORT_TEXT) {
            return null
        }
        return createComplicationData("Preview", "Preview Data").build()
    }

    override fun onComplicationRequest(
        request: ComplicationRequest, listener: ComplicationRequestListener
    ) {
        Log.d(TAG, "Complication Data: $currentSOTWValue")
        listener.onComplicationData(
            createComplicationData(
                currentSOTWValue,
                "Compass Data"
            ).setTapAction(createOnTapPendingIntent()).build()
        )
    }



    private fun createOnTapPendingIntent(): PendingIntent? {
//        val updateIntent = Intent("com.iniyan.wearcompass.action.UPDATE_COMPLICATION")
        val updateIntent = Intent("com.iniyan.wearcompass.MESSAGE_RECEIVED")
        return PendingIntent.getBroadcast(this, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createComplicationData(text: String, contentDescription: String) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text).build(),
            contentDescription = PlainComplicationText.Builder(contentDescription).build()
        )
}
