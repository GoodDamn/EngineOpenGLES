package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.GLIShaderNormal
import good.damn.common.matrices.COMatrixNormal

data class GLDrawerNormalMatrix(
    var matrixNormal: COMatrixNormal
) {
    companion object {
        @JvmStatic
        fun draw(
            drawerNormal: GLDrawerNormalMatrix,
            shader: GLIShaderNormal
        ) {
            GLES30.glUniformMatrix4fv(
                shader.uniformNormalMatrix,
                1,
                false,
                drawerNormal.matrixNormal.normalMatrix,
                0
            )
        }
    }
}