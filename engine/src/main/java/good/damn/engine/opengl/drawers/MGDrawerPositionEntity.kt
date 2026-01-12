package good.damn.engine.opengl.drawers

import android.opengl.GLES30.*
import good.damn.common.matrices.COMatrixModel
import good.damn.engine.opengl.shaders.MGIShaderModel

class MGDrawerPositionEntity(
    var modelMatrix: COMatrixModel
): MGIDrawerShader<MGIShaderModel> {

    companion object {
        fun draw(
            shader: MGIShaderModel,
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
        shader: MGIShaderModel
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