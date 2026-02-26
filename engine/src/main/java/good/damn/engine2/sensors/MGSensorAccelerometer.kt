package good.damn.engine2.sensors

import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log

class MGSensorAccelerometer
: MGISensor {
    override val type = MGEnumSensor.ACCELEROMETER
    override val delay = MGEnumSensorDelay.GAME

    override fun processChanges(
        values: FloatArray
    ) {
        Log.d("TAG", "processChanges: ${values.contentToString()}")
    }

}