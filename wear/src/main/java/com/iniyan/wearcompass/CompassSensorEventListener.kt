package com.iniyan.wearcompass

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder

class CompassSensorEventListener : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var magnetometer: Sensor
    private lateinit var accelerometer: Sensor

    private var lastAccelerometer: FloatArray = FloatArray(3)
    private var lastMagnetometer: FloatArray = FloatArray(3)
    private var lastAccelerometerSet: Boolean = false
    private var lastMagnetometerSet: Boolean = false
    private var rotationMatrix: FloatArray = FloatArray(9)
    private var orientation: FloatArray = FloatArray(3)
    private var azimuthInRadians: Float = 0F
    private var azimuthInDegrees: Float = 0F

    override fun onBind(intent: Intent?): IBinder? {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor == magnetometer) {
                lastMagnetometer.copyInto(event.values, 0, 0, event.values.size)
                lastMagnetometerSet = true
            } else if (event.sensor == accelerometer) {
                lastAccelerometer.copyInto(event.values, 0, 0, event.values.size)
                lastAccelerometerSet = true
            }
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            SensorManager.getRotationMatrix(
                rotationMatrix,
                null,
                lastAccelerometer,
                lastMagnetometer
            )
            SensorManager.getOrientation(rotationMatrix, orientation)

            azimuthInRadians = orientation[0]
            azimuthInDegrees = ((Math.toDegrees(azimuthInRadians.toDouble()) + 360) % 360).toFloat()
        }
    }

    fun getAzimuthInDegrees(): Float {
        return azimuthInDegrees
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

}
