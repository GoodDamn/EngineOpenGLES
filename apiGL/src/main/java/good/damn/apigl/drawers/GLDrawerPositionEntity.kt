package good.damn.apigl.drawers

import android.opengl.GLES30.*
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.common.matrices.COMatrixModel

data class GLDrawerPositionEntity(
    var modelMatrix: COMatrixModel
) {
    companion object {
        @JvmStatic
        fun draw(
            shader: GLIShaderModel,
            modelMatrix: COMatrixModel
        ) {
            glUniformMatrix4fv(
                shader.uniformModelView,
                1,
                false,
                modelMatrix.model,
                0
            )
        }
    }
}