package good.damn.engine.opengl.camera

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIUniform
import good.damn.engine.opengl.entities.MGObjectDimension
import good.damn.engine.opengl.shaders.MGIShader

open class MGCamera(
    var shader: MGIShader,
    var modelMatrix: MGMMatrix
): MGIDrawer {

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
            0.1f,
            1000000f
        )
    }

    override fun draw() {
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