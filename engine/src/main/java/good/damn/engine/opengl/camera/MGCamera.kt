package good.damn.engine.opengl.camera

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIUniform
import good.damn.engine.opengl.entities.MGObjectDimension

open class MGCamera(
    var modelMatrix: MGMMatrix
): MGIDrawer,
MGIUniform {

    private val mProjection = FloatArray(
        16
    )

    private var mUniformProject = 0
    private var mUniformCamera = 0

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

    override fun setupUniforms(
        program: Int
    ) {
        mUniformProject = glGetUniformLocation(
            program,
            "projection"
        )

        mUniformCamera = glGetUniformLocation(
            program,
            "view"
        )
    }

    override fun draw() {
        glUniformMatrix4fv(
            mUniformProject,
            1,
            false,
            mProjection,
            0
        )

        glUniformMatrix4fv(
            mUniformCamera,
            1,
            false,
            modelMatrix.model,
            0
        )
    }

}