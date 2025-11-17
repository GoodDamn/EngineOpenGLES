package good.damn.engine.ui.clicks

import good.damn.engine.MGEngine
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.callbacks.MGCallbackOnCameraMovement
import good.damn.engine.opengl.callbacks.MGCallbackOnDeltaInteract
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.ui.MGIClick
import java.util.concurrent.ConcurrentLinkedQueue

class MGClickPlaceMesh(
    private val bridge: MGBridgeRayIntersect
): MGIClick {

    override fun onClick() {
        if (MGEngine.drawMode != MGEnumDrawMode.OPAQUE) {
            return
        }

        if (bridge.matrix == null) {
            return
        }

        bridge.matrix = null
    }

}