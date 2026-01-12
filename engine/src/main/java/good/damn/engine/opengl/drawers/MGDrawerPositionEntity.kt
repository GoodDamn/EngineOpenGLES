package good.damn.engine.opengl.drawers

import android.opengl.GLES30.*
import good.damn.common.matrices.MGMatrixModel
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.shaders.MGIShaderModel

class MGDrawerPositionEntity(
    var modelMatrix: MGMatrixModel
): MGIDrawerShader<MGIShaderModel> {

    companion object {
        fun draw(
            shader: MGIShaderModel,
            modelMatrix: MGMatrixModel
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