package good.damn.engine.opengl.callbacks

import good.damn.engine.opengl.matrices.MGMMatrix
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.touch.MGIListenerDelta

class MGCallbackOnDeltaInteract
: MGIListenerDelta {

    var currentMeshInteract: MGMatrixScale? = null

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