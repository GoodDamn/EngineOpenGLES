package good.damn.engine.opengl.callbacks

import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.touch.MGIListenerDelta

class MGCallbackOnDeltaInteract(
    private val bridge: MGBridgeRayIntersect
): MGIListenerDelta {

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        /*currentMeshInteract?.addRotation(
            dx * 0.5f,
            0.0f
        )*/
    }
}