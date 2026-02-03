package good.damn.engine2.sensors

import android.hardware.SensorManager

enum class MGEnumSensorDelay(
    val v: Int,
    val milliseconds: Float
) {
    GAME(
        SensorManager.SENSOR_DELAY_GAME,
        20 / 1000f
    )
}