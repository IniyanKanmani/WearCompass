package com.iniyan.wearcompass

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.iniyan.wearcompass.complication.MainComplicationService
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.roundToInt

class DailyMoonPhaseWorker(context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {

    private var moonPhaseInPercentage: Int? = null

    override fun doWork(): Result {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://moon-phase.p.rapidapi.com/advanced?lat=52.4679&lon=-1.9974")
            .get()
            .addHeader("X-RapidAPI-Key", "cda709181amshfcf538072300ba5p154e36jsnee452ae4564d")
            .addHeader("X-RapidAPI-Host", "moon-phase.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.let {

                val parser = Parser()
                val json = parser.parse(StringBuilder(it.string())) as JsonObject

                val moonPhase = json.obj("moon")?.double("phase")
                moonPhaseInPercentage = (moonPhase?.times(100))?.roundToInt()
                MainComplicationService.todayMoonPhaseValue = moonPhaseInPercentage.toString()
            }

        }
        return Result.success()
    }
}