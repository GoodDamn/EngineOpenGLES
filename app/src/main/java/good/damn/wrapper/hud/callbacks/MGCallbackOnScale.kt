package good.damn.wrapper.hud.callbacks

import good.damn.wrapper.hud.bridges.MGBridgeRayIntersect
import good.damn.hud.touch.MGIListenerScale

class MGCallbackOnScale(
   private val bridgeMatrix: MGBridgeRayIntersect
): MGIListenerScale {

    override fun onScale(
        dtScale: Float
    ) {
        bridgeMatrix.intersectUpdate?.updateScale(
            dtScale
        )
    }

}