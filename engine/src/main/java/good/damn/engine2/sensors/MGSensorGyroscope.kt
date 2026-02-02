package good.damn.engine2.sensors

import android.util.Log
import good.damn.common.camera.COICameraFree

class MGSensorGyroscope(
    private val cameraFree: COICameraFree
): MGISensor {

    companion object {
        private const val SCALE = -0.025f
    }

    override val type = MGEnumSensor.GYROSCOPE
    override val delay = MGEnumSensorDelay.GAME

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