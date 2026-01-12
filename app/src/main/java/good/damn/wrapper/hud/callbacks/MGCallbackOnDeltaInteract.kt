package good.damn.wrapper.hud.callbacks

import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.hud.touch.MGIListenerDelta

class MGCallbackOnDeltaInteract(
    private val bridge: APBridgeRayIntersect
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