package good.damn.opengles_engine.opengl.camera

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.opengles_engine.opengl.entities.DimensionObject

open class BaseCamera
: DimensionObject() {

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

    fun draw(
        unifProj: Int,
        unifModel: Int,
        unifCamera: Int,
        model: FloatArray
    ) {
        glUniformMatrix4fv(
            unifProj,
            1,
            false,
            mProjection,
            0
        )

        glUniformMatrix4fv(
            unifCamera,
            1,
            false,
            this.model,
            0
        )

        glUniformMatrix4fv(
            unifModel,
            1,
            false,
            model,
            0
        )
    }

}