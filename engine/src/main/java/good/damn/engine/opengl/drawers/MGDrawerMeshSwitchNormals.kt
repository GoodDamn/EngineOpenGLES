package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import good.damn.common.matrices.COMatrixNormal
import good.damn.engine.opengl.shaders.MGIShaderNormal

class MGDrawerMeshSwitchNormals(
    vertexArray: MGDrawerVertexArray,
    drawEntity: MGDrawerPositionEntity,
    frontFace: Int,
    private val matrixNormal: COMatrixNormal
): MGDrawerMeshSwitch(
    vertexArray,
    drawEntity,
    frontFace
) {
    override fun drawNormals(
        shader: MGIShaderNormal
    ) {
        GLES30.glUniformMatrix4fv(
            shader.uniformNormalMatrix,
            1,
            false,
            matrixNormal.normalMatrix,
            0
        )
    }
}