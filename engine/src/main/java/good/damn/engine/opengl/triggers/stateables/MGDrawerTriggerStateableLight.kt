package good.damn.engine.opengl.triggers.stateables

import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.entities.MGLight
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.triggers.MGMatrixTriggerLight
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerState

class MGDrawerTriggerStateableLight(
    val light: MGLight,
    val stateManager: MGManagerTriggerState,
    drawerVertexArray: MGDrawerVertexArray,
    shader: MGIShaderModel,
    val modelMatrix: MGMatrixTriggerLight
): MGIDrawer {
    private val mEntity = MGDrawerPositionEntity(
        drawerVertexArray,
        shader,
        modelMatrix.matrixTrigger.model
    )

    override fun draw() {
        mEntity.draw()
    }
}