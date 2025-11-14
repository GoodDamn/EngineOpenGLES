package good.damn.engine.opengl.camera

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.shaders.MGIShaderCamera
import good.damn.engine.opengl.shaders.MGIShaderCameraPosition

open class MGCamera(
    var modelMatrix: MGMatrixTranslate
) {

    private val mProjection = FloatArray(
        16
    )

    fun setPerspective(
        width: Int,
        height: Int
    ) {
        Matrix.perspectiveM(
            mProjection,
            0,
            85.0f,
            width.toFloat() / height.toFloat(),
            0.9999999f,
            Float.MAX_VALUE / 2
        )
    }

    fun drawPosition(
        shader: MGIShaderCameraPosition
    ) {
        glUniform3f(
            shader.uniformCameraPosition,
            modelMatrix.x,
            modelMatrix.y,
            modelMatrix.z
        )
    }

    fun draw(
        shader: MGIShaderCamera
    ) {
        glUniformMatrix4fv(
            shader.uniformCameraProjection,
            1,
            false,
            mProjection,
            0
        )

        glUniformMatrix4fv(
            shader.uniformCameraView,
            1,
            false,
            modelMatrix.model,
            0
        )
    }
}