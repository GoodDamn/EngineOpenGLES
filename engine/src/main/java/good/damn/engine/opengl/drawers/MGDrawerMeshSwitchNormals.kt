package good.damn.engine.opengl.drawers

import good.damn.common.matrices.MGMatrixNormal
import good.damn.engine.opengl.shaders.MGIShaderNormal

class MGDrawerMeshSwitchNormals(
    vertexArray: MGDrawerVertexArray,
    drawEntity: MGDrawerPositionEntity,
    frontFace: Int,
    private val matrixNormal: MGMatrixNormal
): MGDrawerMeshSwitch(
    vertexArray,
    drawEntity,
    frontFace
) {
    override fun drawNormals(
        shader: MGIShaderNormal
    ) {
        matrixNormal.draw(
            shader
        )
    }
}