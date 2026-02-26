package good.damn.engine2.sensors

interface MGISensor {
    val type: MGEnumSensor
    val delay: MGEnumSensorDelay

    fun processChanges(
        values: FloatArray
    )
}