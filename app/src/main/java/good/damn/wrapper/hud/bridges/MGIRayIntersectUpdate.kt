package good.damn.wrapper.hud.bridges

import good.damn.engine.sdk.SDVector3

interface MGIRayIntersectUpdate {
    fun updateDelta(
        dx: Float,
        dy: Float
    )

    fun updateIntersectPosition(
        v: SDVector3
    )

    fun updateScale(
        dtScale: Float
    )
}