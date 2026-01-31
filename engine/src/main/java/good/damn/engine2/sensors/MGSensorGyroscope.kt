package good.damn.engine2.sensors

import android.util.Log

class MGSensorGyroscope
: MGISensor {
    override val type = MGEnumSensor.GYROSCOPE
    override val delay = MGEnumSensorDelay.GAME

    override fun processChanges(
        values: FloatArray
    ) {

    }
}