package good.damn.apigl.drawers

import android.opengl.GLES30.*
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.engine.sdk.matrices.SDMatrixModel

data class GLDrawerPositionEntity(
    var modelMatrix: SDMatrixModel
) {
    companion object {
        @JvmStatic
        fun draw(
            shader: GLIShaderModel,
            modelMatrix: SDMatrixModel
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