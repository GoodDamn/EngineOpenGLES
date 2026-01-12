package good.damn.wrapper.hud.callbacks

import good.damn.common.camera.COICameraFree
import good.damn.engine.sdk.SDVector3
import good.damn.hud.touch.MGIListenerDelta
import good.damn.hud.touch.MGIListenerDistance
import good.damn.hud.touch.MGIListenerMove
import good.damn.wrapper.hud.bridges.MGBridgeRayIntersect

class MGCallbackOnCameraMovement(
    private val camera: COICameraFree,
    private val bridge: MGBridgeRayIntersect
): MGIListenerDelta,
MGIListenerMove,
MGIListenerDistance {

    private var mListenerIntersect: MGIListenerOnIntersectPosition? = null

    fun setListenerIntersection(
        v: MGIListenerOnIntersectPosition?
    ) {
        mListenerIntersect = v
    }

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        camera.addRotation(
            dx * 0.001f,
            dy * 0.001f
        )
        camera.invalidatePosition()
        updateIntersection()
    }

    override fun onMove(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    ) {
        camera.addPosition(
            x, y,
            directionX,
            directionY
        )

        camera.invalidatePosition()

        updateIntersection()
    }

    override fun onDistance(
        dst: Float
    ) {
        bridge.distance += dst
        camera.invalidatePosition()
        updateIntersection()
    }

    private inline fun updateIntersection() {
        val direction = camera.direction
        bridge.outPointLead.apply {
            x = camera.modelMatrix.x + direction.x * bridge.distance
            y = camera.modelMatrix.y + direction.y * bridge.distance
            z = camera.modelMatrix.z + direction.z * bridge.distance
        }

        mListenerIntersect?.onIntersectPosition(
            bridge.outPointLead
        )
    }

}