package good.damn.engine.opengl.triggers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.matrices.MGMMatrix
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGIShaderModel

class MGDrawerTriggerStateable(
    val stateManager: MGManagerTriggerState,
    vertexArray: MGArrayVertex,
    shader: MGIShaderModel,
    modelMatrix: MGMMatrix
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