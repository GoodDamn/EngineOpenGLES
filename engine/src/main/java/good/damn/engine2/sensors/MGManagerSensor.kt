package good.damn.engine2.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class MGManagerSensor(
    private val sensors: List<MGISensor>
) {
    private val mObserver = MGSensorObserver()

    fun register(
        sensorManager: SensorManager
    ) {
        sensors.forEach {
            sensorManager.registerListener(
                mObserver,
                sensorManager.getDefaultSensor(
                    it.type.v
                ),
                it.delay.v
            )
        }
    }

    fun unregister(
        sensorManager: SensorManager
    ) {
        sensorManager.unregisterListener(
            mObserver
        )
    }

    private inner class MGSensorObserver
    : SensorEventListener {

        override fun onSensorChanged(
            event: SensorEvent?
        ) {
            event ?: return

            sensors.forEach {
                if (it.type.v == event.sensor.type) {
                    it.processChanges(
                        event.values
                    )
                }
            }
        }

        override fun onAccuracyChanged(
            sensor: Sensor?,
            accuracy: Int
        ) = Unit
    }
}