package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.GLIShaderNormal
import good.damn.common.matrices.COMatrixNormal

class GLDrawerMeshNormals(
    vertexArray: GLDrawerVertexArray,
    drawEntity: GLDrawerPositionEntity,
    frontFace: Int,
    private val matrixNormal: COMatrixNormal
): GLDrawerMesh(
    vertexArray,
    drawEntity,
    frontFace
) {
    override fun drawNormals(
        shader: GLIShaderNormal
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