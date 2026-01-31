package good.damn.engine2.sensors

import android.hardware.SensorManager

enum class MGEnumSensorDelay(
    val v: Int
) {
    GAME(
        SensorManager.SENSOR_DELAY_GAME
    )
}