package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.drawers.MGIUniform

open class MGMesh
: MGObjectDimension(),
MGIUniform {

    companion object {
        internal const val mStride = 8 * 4
    }

    protected var mTextureOffset = 1f

    private var mUniformModelView = 0
    private var mUniformTextureOffset = 0

    init {
        Matrix.setIdentityM(
            model,
            0
        )
    }

    override fun setupUniforms(
        program: Int
    ) {
        mUniformModelView = glGetUniformLocation(
            program,
            "model"
        )

        mUniformTextureOffset = glGetUniformLocation(
            program,
            "textureOffset"
        )
    }

    open fun draw(
        camera: MGCamera
    ) {
        glUniform1f(
            mUniformTextureOffset,
            mTextureOffset
        )

        glUniformMatrix4fv(
            mUniformModelView,
            1,
            false,
            model,
            0
        )

        //camera.draw()
    }

}