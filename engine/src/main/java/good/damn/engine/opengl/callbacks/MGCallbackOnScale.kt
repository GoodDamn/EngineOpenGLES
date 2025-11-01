package good.damn.engine.opengl.callbacks

import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.touch.MGIListenerScale

class MGCallbackOnScale(
   private val bridgeMatrix: MGBridgeRayIntersect
): MGIListenerScale {
    override fun onScale(
        scale: Float
    ) {
        bridgeMatrix.matrix?.run {
            setScale(
                scale,
                scale,
                scale
            )
            invalidateScaleRotation()
            invalidatePosition()
            calculateInvertTrigger()
            calculateNormals()
        }
    }
}