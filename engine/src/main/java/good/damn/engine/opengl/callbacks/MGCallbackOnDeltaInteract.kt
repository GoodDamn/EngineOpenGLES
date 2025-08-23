package good.damn.engine.opengl.callbacks

import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.touch.MGIListenerDelta

class MGCallbackOnDeltaInteract
: MGIListenerDelta {

    var currentMeshInteract: MGMMatrix? = null

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        currentMeshInteract?.addRotation(
            dx * 0.5f,
            0.0f
        )
    }
}