package good.damn.engine.opengl.triggers.stateables

import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerStateCallback

class MGDrawerTriggerStateable(
    val stateManager: MGManagerTriggerStateCallback,
    val modelMatrix: MGMatrixScaleRotation
): MGIDrawerShader<MGIShaderModel> {
    private val mEntity = MGDrawerPositionEntity(
        modelMatrix
    )

    override fun draw(
        shader: MGIShaderModel
    ) {
        mEntity.draw(
            shader
        )
    }
}