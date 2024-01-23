package com.iniyan.wearcompass

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import com.iniyan.wearcompass.complication.MainComplicationService

class CompassDataForegroundService : Service() {
    private companion object {
        const val TAG = "WearCompassForegroundService"
    }

    private val startMessageAction: String = "com.iniyan.wearcompass.START_COMPASS_DATA"
    private val stopMessageAction: String = "com.iniyan.wearcompass.STOP_COMPASS_DATA"

    private lateinit var notificationManager: NotificationManager

    private lateinit var compass: Compass
    private lateinit var sotwFormatter: SOTWFormatter

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d(TAG, "OnCreate")
        super.onCreate()
        notificationManager = getSystemService(NotificationManager::class.java)

        sotwFormatter = SOTWFormatter(this)
        setupCompass()

//        val startFilter = IntentFilter(startMessageAction)
//        val stopFilter = IntentFilter(startMessageAction)
//        registerReceiver(MainBroadcastReceiver(), startFilter, RECEIVER_NOT_EXPORTED)
//        registerReceiver(MainBroadcastReceiver(), stopFilter, RECEIVER_NOT_EXPORTED)

        val timer: CountDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("break", "tick-tock")
            }

            override fun onFinish() {
//                val intent = Intent(stopMessageAction)
//                sendBroadcast(intent)
                MainComplicationService.complicationDataToDisplay = MainComplicationService.todayMoonPhaseValue
                ComplicationDataSourceUpdateRequester.create(
                    applicationContext, ComponentName(applicationContext, MainComplicationService::class.java)
                ).requestUpdateAll()
                stopForeground(true)
                stopSelf()
            }

        }
        timer.start()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "OnCreate")
        super.onStartCommand(intent, flags, startId)

        createNotificationChannel()

        val notification: Notification =
            NotificationCompat.Builder(this, "ChannelId1").setContentTitle("Wear Compass")
                .setContentText("Listening to compass data").setSmallIcon(R.mipmap.ic_launcher)
                .build()

        startForeground(1, notification)
        compass.start()

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val notificationChannel: NotificationChannel = NotificationChannel(
            "ChannelId1", "Compass Foreground Notification", NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun setupCompass() {
        compass = Compass(this)
        compass.setListener(getCompassListener())
    }

    private fun getCompassListener() = object : Compass.CompassListener {
        override fun onNewAzimuth(azimuth: Float) {
//            val intent = Intent(startMessageAction).apply {
//                putExtra("message", sotwFormatter.format(azimuth))
//            }
            MainComplicationService.complicationDataToDisplay = sotwFormatter.format(azimuth)
            Log.d(TAG, "Update Data: ${sotwFormatter.format(azimuth)}")
            ComplicationDataSourceUpdateRequester.create(
                applicationContext, ComponentName(applicationContext, MainComplicationService::class.java)
            ).requestUpdateAll()
//            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }
}