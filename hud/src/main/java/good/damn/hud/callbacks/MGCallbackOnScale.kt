package good.damn.hud.touch.callbacks

import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
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