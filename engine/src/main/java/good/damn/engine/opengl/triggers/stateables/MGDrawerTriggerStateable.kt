package good.damn.engine.opengl.triggers.stateables

import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerStateCallback

class MGDrawerTriggerStateable(
    val stateManager: MGManagerTriggerStateCallback,
    drawerVertexArray: MGDrawerVertexArray,
    shader: MGIShaderModel,
    val modelMatrix: MGMatrixScaleRotation
): MGIDrawer {
    private val mEntity = MGDrawerPositionEntity(
        drawerVertexArray,
        shader,
        modelMatrix
    )

    override fun draw() {
        mEntity.draw()
    }
}