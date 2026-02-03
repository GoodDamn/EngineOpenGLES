package good.damn.engine2.sensors

import android.util.Log
import good.damn.common.camera.COICameraFree
import kotlin.math.PI

class MGSensorGyroscope(
    private val cameraFree: COICameraFree
): MGISensor {

    companion object {
        private val DELAY = MGEnumSensorDelay.GAME
        private val SCALE = -DELAY.milliseconds
    }

    override val type = MGEnumSensor.GYROSCOPE
    override val delay = DELAY

    override fun processChanges(
        values: FloatArray
    ) {
        cameraFree.addRotation(
            values[0] * SCALE,
            values[1] * SCALE,
            values[2] * SCALE
        )

        cameraFree.invalidatePosition()
    }
}