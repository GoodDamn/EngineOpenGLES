package good.damn.engine2.sensors

import android.util.Log
import good.damn.common.camera.COICameraFree
import kotlin.math.PI

class MGSensorGyroscope(
    private val cameraFree: COICameraFree
): MGISensor {

    override val type = MGEnumSensor.GYROSCOPE
    override val delay = MGEnumSensorDelay.GAME

    override fun processChanges(
        values: FloatArray
    ) {
        cameraFree.addRotation(
            values[0] * -delay.milliseconds,
            values[1] * -delay.milliseconds,
            0f
        )

        cameraFree.invalidatePosition()
    }
}