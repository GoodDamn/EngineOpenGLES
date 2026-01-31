package good.damn.engine2.sensors

import android.hardware.Sensor

enum class MGEnumSensor(
    val v: Int
) {
    ACCELEROMETER(
        Sensor.TYPE_ACCELEROMETER
    ),
    GYROSCOPE(
        Sensor.TYPE_GYROSCOPE
    )
}