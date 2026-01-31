package good.damn.engine2.sensors

import android.util.Log
import good.damn.common.camera.COICameraFree

class MGSensorGyroscope(
    private val cameraFree: COICameraFree
): MGISensor {
    override val type = MGEnumSensor.GYROSCOPE
    override val delay = MGEnumSensorDelay.GAME

    override fun processChanges(
        values: FloatArray
    ) {
        cameraFree.addRotation(
            values[0] * -0.025f,
            values[1] * -0.025f
        )

        cameraFree.invalidatePosition()
    }
}