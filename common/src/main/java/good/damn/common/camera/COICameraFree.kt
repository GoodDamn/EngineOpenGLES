package good.damn.common.camera

import good.damn.engine.sdk.SDVector3

interface COICameraFree
: COICameraPosition {
    val direction: SDVector3

    fun invalidatePosition()

    fun addPosition(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    )

    fun addRotation(
        yaw: Float,
        pitch: Float,
        roll: Float
    )

}