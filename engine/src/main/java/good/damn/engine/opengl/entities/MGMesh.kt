package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.engine.opengl.camera.MGCamera

open class MGMesh(
    program: Int
): MGObjectDimension() {

    companion object {
        internal const val mStride = 8 * 4
    }

    protected var mTextureOffset = 1f

    private val mUniformModelView = glGetUniformLocation(
        program,
        "model"
    )

    private val mUniformProject = glGetUniformLocation(
        program,
        "projection"
    )

    private val mUniformCamera = glGetUniformLocation(
        program,
        "view"
    )

    private val mUniformTextureOffset = glGetUniformLocation(
        program,
        "textureOffset"
    )

    init {
        Matrix.setIdentityM(
            model,
            0
        )
    }

    open fun draw(
        camera: MGCamera
    ) {
        glUniform1f(
            mUniformTextureOffset,
            mTextureOffset
        )

        camera.draw(
            mUniformProject,
            mUniformModelView,
            mUniformCamera,
            model
        )
    }

}