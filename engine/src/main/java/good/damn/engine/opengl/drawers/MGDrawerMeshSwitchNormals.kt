package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.shaders.MGIShaderNormal
import java.nio.Buffer
import java.nio.IntBuffer

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