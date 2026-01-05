package good.damn.engine.touch.callbacks

import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
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