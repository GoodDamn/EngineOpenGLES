package good.damn.engine2.sensors

import android.util.Log
import good.damn.common.camera.COICameraFree
import good.damn.engine2.providers.MGProviderGL
import kotlin.math.PI

class MGSensorGyroscope
: MGProviderGL(), MGISensor {

    companion object {
        private val DELAY = MGEnumSensorDelay.GAME
        private val SCALE = -DELAY.milliseconds
    }

    override val type = MGEnumSensor.GYROSCOPE
    override val delay = DELAY

    override fun processChanges(
        values: FloatArray
    ) {
        glProvider.camera.apply {
            addRotation(
                values[0] * SCALE,
                values[1] * SCALE,
                values[2] * SCALE
            )
            invalidatePosition()
        }
    }
}