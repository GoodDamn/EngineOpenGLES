package good.damn.hud.touch.callbacks

import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.hud.touch.MGIListenerDelta

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