package good.damn.wrapper.hud.callbacks

import good.damn.wrapper.hud.bridges.MGBridgeRayIntersect
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