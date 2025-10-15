package good.damn.engine.opengl.triggers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.shaders.MGIShaderModel

class MGDrawerTriggerStateable(
    val stateManager: MGManagerTriggerStateCallback,
    vertexArray: MGArrayVertex,
    shader: MGIShaderModel,
    val modelMatrix: MGMatrixScaleRotation
): MGIDrawer {
    private val mEntity = MGDrawerPositionEntity(
        MGDrawerVertexArray(
            vertexArray
        ),
        shader,
        modelMatrix
    )

    override fun draw() {
        mEntity.draw()
    }
}