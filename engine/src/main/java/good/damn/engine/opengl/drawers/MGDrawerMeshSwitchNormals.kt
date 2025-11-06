package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.shaders.MGIShaderNormal

class MGDrawerMeshSwitchNormals(
    vertexArray: MGArrayVertex,
    drawEntity: MGDrawerPositionEntity,
    frontFace: Int,
    private val matrixNormal: MGMatrixNormal
): MGDrawerMeshSwitch(
    vertexArray,
    drawEntity,
    frontFace
) {

    override fun switchDrawMode(
        shader: MGIShaderNormal?
    ) {
        matrixNormal.shader = shader
    }

    override fun draw() {
        matrixNormal.draw()
        super.draw()
    }
}