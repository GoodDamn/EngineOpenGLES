package good.damn.engine.ui.clicks

import good.damn.engine.MGEngine
import good.damn.engine.opengl.callbacks.MGCallbackOnCameraMovement
import good.damn.engine.opengl.callbacks.MGCallbackOnDeltaInteract
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.ui.MGIClick
import java.util.concurrent.ConcurrentLinkedQueue

class MGClickPlaceMesh(
    private val callbackOnDeltaInteract: MGCallbackOnDeltaInteract,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshSwitch>,
    private val drawerSwitchBatch: MGDrawerModeSwitch,
    private val shaderDefault: MGShaderDefault,
    private val callbackCameraMove: MGCallbackOnCameraMovement
): MGIClick {

    override fun onClick() {
        if (MGEngine.drawMode != MGEnumDrawMode.OPAQUE) {
            return
        }

        if (callbackOnDeltaInteract.currentMeshInteract != null) {
            callbackOnDeltaInteract.currentMeshInteract = null
            return
        }

        val modelMatrix = MGMatrixScale().apply {
            setScale(
                0.01f,
                0.01f,
                0.01f
            )
        }

        val matrixNormal = MGMatrixNormal(
            shaderDefault,
            modelMatrix.model
        )

        callbackOnDeltaInteract.currentMeshInteract = modelMatrix
        meshes.add(
            MGDrawerMeshSwitch(
                drawerSwitchBatch,
                MGDrawerPositionEntity(
                    drawerSwitchBatch,
                    shaderDefault,
                    modelMatrix
                ),
                matrixNormal
            )
        )

        val p = callbackCameraMove.outPointLead
        modelMatrix.setPosition(
            p.x,
            p.y,
            p.z
        )
    }

}