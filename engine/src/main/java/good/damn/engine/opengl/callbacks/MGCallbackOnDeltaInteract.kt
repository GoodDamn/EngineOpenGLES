package good.damn.engine.opengl.callbacks

import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.touch.MGIListenerDelta

class MGCallbackOnDeltaInteract(
    private val bridge: MGBridgeRayIntersect
): MGIListenerDelta {

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        bridge.intersectUpdate?.updateDelta(
            dx, dy
        )
    }
}