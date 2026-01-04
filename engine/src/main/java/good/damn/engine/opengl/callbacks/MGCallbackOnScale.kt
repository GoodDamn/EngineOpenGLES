package good.damn.engine.opengl.callbacks

import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.touch.MGIListenerScale

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