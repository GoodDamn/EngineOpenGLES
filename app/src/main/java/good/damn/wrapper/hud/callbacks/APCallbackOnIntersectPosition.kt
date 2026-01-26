package good.damn.wrapper.hud.callbacks

import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.engine.sdk.SDVector3

class APCallbackOnIntersectPosition(
    private val bridgeMatrix: APBridgeRayIntersect
): APIListenerOnIntersectPosition {

    override fun onIntersectPosition(
        p: SDVector3
    ) {
        bridgeMatrix.intersectUpdate?.updateIntersectPosition(
            p
        )
    }

}