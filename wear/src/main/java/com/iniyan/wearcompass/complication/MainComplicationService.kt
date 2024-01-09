package com.iniyan.wearcompass.complication

import android.content.Intent
import android.util.Log
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService

/**
 * Skeleton for complication data source that returns short text.
 */
class MainComplicationService : SuspendingComplicationDataSourceService() {

    companion object {
        const val TAG = "WearCompass"
        var currentSOTWValue = ""
    }


    override fun onCreate() {
        Log.d(TAG, "onCreate")

        val tapBuilder =

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
        return createComplicationData("Preview", "Preview Data")
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        Log.d(TAG, "Complication Data: $currentSOTWValue")
        return createComplicationData(currentSOTWValue, "Compass Data")
    }

    private fun createComplicationData(text: String, contentDescription: String) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text).build(),
            contentDescription = PlainComplicationText.Builder(contentDescription).build()
        ).build()

}