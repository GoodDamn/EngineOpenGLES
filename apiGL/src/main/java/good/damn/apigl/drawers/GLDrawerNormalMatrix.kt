package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.GLIShaderNormal
import good.damn.engine.sdk.matrices.SDMatrixNormal

data class GLDrawerNormalMatrix(
    var matrixNormal: SDMatrixNormal
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