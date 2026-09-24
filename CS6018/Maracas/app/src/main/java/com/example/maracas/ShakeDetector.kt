package com.example.maracas

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.sqrt

class ShakeDetector(
    context: Context,
    private val onShakeDetected: (ShakeEntity) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var baselineAcceleration = 9.81f
    private var lastShakeTime = 0L
    private val COOLDOWN_MS = 300L

    // 1. Lowered threshold (from 15f to 4.5f) for high sensitivity
    private val SHAKE_THRESHOLD = 4.5f
    private val baselineReadings = mutableListOf<Float>()
    private val BASELINE_SAMPLE_SIZE = 20

    fun start() {
        // 2. Changed to SENSOR_DELAY_FASTEST for better emulator responsiveness
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val magnitude = sqrt(
            event.values[0] * event.values[0] +
                    event.values[1] * event.values[1] +
                    event.values[2] * event.values[2]
        )

        if (baselineReadings.size < BASELINE_SAMPLE_SIZE) {
            baselineReadings.add(magnitude)
            if (baselineReadings.size == BASELINE_SAMPLE_SIZE) {
                baselineAcceleration = baselineReadings.average().toFloat()
            }
            return
        }

        // 3. Added abs() so negative spikes (sudden deceleration/reversals) trigger it too
        val delta = abs(magnitude - baselineAcceleration)
        val currentTime = System.currentTimeMillis()

        if (delta > SHAKE_THRESHOLD && currentTime - lastShakeTime > COOLDOWN_MS) {
            lastShakeTime = currentTime
            onShakeDetected(ShakeEntity(timestamp = currentTime, intensity = delta, accelerationMagnitude = magnitude))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}