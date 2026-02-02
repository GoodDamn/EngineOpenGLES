package good.damn.apigl.drawers

import android.opengl.GLES30.*
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.common.matrices.COMatrixModel

class GLDrawerPositionEntity(
    var modelMatrix: COMatrixModel
): GLIDrawerShader<GLIShaderModel> {

    companion object {
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

    override fun draw(
        shader: GLIShaderModel
    ) {
        synchronized(
            modelMatrix.model
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