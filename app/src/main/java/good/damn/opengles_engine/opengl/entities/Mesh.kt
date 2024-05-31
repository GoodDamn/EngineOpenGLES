package good.damn.opengles_engine.opengl.entities

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.opengles_engine.opengl.camera.BaseCamera

open class Mesh(
    program: Int,
    private val mCamera: BaseCamera
): DimensionObject() {

    companion object {
        internal const val mStride = 8 * 4
    }

    internal var mAttrPosition = 0
    internal var mAttrTexCoord = 0
    internal var mAttrNormal = 0

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
        "camera"
    )

    init {
        Matrix.setIdentityM(
            model,
            0
        )
    }

    open fun draw() {
        mCamera.draw(
            mUniformProject,
            mUniformModelView,
            mUniformCamera,
            model
        )
    }

}