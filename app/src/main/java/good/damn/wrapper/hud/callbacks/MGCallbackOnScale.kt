package good.damn.wrapper.hud.callbacks

import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.hud.touch.UIIListenerScale

class MGCallbackOnScale(
   private val bridgeMatrix: APBridgeRayIntersect
): UIIListenerScale {

    override fun onScale(
        dtScale: Float
    ) {
        bridgeMatrix.intersectUpdate?.updateScale(
            dtScale
        )
    }

}