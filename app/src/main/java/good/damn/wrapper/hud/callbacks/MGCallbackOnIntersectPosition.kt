package good.damn.wrapper.hud.callbacks

import good.damn.wrapper.hud.bridges.MGBridgeRayIntersect
import good.damn.engine.sdk.SDVector3

class MGCallbackOnIntersectPosition(
    private val bridgeMatrix: MGBridgeRayIntersect
): MGIListenerOnIntersectPosition {

    override fun onIntersectPosition(
        p: SDVector3
    ) {
        bridgeMatrix.intersectUpdate?.updateIntersectPosition(
            p
        )
    }

}