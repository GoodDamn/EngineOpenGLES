package good.damn.engine.opengl.callbacks

import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.touch.MGIListenerDelta

class MGCallbackOnDeltaInteract(
    private val bridge: MGBridgeRayIntersect
): MGIListenerDelta {

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        bridge.matrix?.run {
            addRotation(
                0f,
                dx * 0.5f,
                dy * -0.5f
            )
            invalidateScaleRotation()
            calculateInvertTrigger()
            calculateNormalsMesh()
        }
    }
}