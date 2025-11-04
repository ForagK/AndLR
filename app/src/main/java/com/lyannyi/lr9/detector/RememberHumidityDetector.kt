package com.lyannyi.lr9.detector

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun RememberHumidityDetector(
    context: Context,
    onHumidityRise: () -> Unit,
    baseHumidity: Float = 50f,
    intervalMs: Long = 1000
) {
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val humiditySensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) }
    var lastTriggerTime by remember { mutableStateOf(0L) }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val currentHumidity = it.values[0]
                    val currentTime = System.currentTimeMillis()
                    if (currentHumidity > baseHumidity && currentTime - lastTriggerTime > intervalMs) {
                        lastTriggerTime = currentTime

                        onHumidityRise()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(listener, humiditySensor, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
}
