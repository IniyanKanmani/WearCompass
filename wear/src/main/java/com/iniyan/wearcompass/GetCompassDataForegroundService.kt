package com.iniyan.wearcompass

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class GetCompassDataForegroundService : Service() {
    private companion object {
        const val TAG = "WearCompassForegroundService"
    }

    private val messageAction: String = "com.iniyan.wearcompass.MESSAGE_RECEIVED"

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

        val filter = IntentFilter(messageAction)
        registerReceiver(TransferCompassDataBroadcastReceiver(), filter, RECEIVER_NOT_EXPORTED)
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
            val intent = Intent(messageAction).apply {
                putExtra("message", sotwFormatter.format(azimuth))
            }
            sendBroadcast(intent)
        }
    }

    private fun adjustSotwLabel(azimuth: Float) {
        Log.d(TAG, "From Service :" + sotwFormatter.format(azimuth))
    }

    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }
}