package good.damn.common.camera

interface COICameraFree {

    fun invalidatePosition()

    fun addPosition(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    )

    fun addRotation(
        yaw: Float,
        pitch: Float
    )

}