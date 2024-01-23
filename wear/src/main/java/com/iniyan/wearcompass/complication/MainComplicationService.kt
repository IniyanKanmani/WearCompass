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
import com.iniyan.wearcompass.CompassDataForegroundService

/**
 * Skeleton for complication data source that returns short text.
 */
class MainComplicationService : ComplicationDataSourceService() {

    companion object {
        const val TAG = "WearCompass"
//        var currentSOTWValue = "SOTW"
        var todayMoonPhaseValue = "Moon"
        var complicationDataToDisplay = ""
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStart")
        complicationDataToDisplay = todayMoonPhaseValue
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
        Log.d(TAG, "Complication Data: $complicationDataToDisplay")
        listener.onComplicationData(
            createComplicationData(
                complicationDataToDisplay,
                "Compass Data"
            ).setTapAction(createOnTapPendingIntent()).build()
        )
    }



//        val updateIntent = Intent("com.iniyan.wearcompass.action.UPDATE_COMPLICATION")
//        val updateIntent = Intent("com.iniyan.wearcompass.START_COMPASS_DATA")
//        val updateIntent = Intent("android.support.wearable.complications.ACTION_COMPLICATION_UPDATE_REQUEST")
    private fun createOnTapPendingIntent(): PendingIntent? {
        val updateIntent = Intent(this, CompassDataForegroundService::class.java)
        return PendingIntent.getForegroundService(this, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        return PendingIntent.getBroadcast(this, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createComplicationData(text: String, contentDescription: String) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text).build(),
            contentDescription = PlainComplicationText.Builder(contentDescription).build()
        )
}
