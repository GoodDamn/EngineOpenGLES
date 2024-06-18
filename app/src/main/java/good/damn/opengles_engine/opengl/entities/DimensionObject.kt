package good.damn.opengles_engine.opengl.entities

import android.opengl.Matrix
import android.opengl.Matrix.*
import android.util.Log
import good.damn.opengles_engine.opengl.Vector

open class DimensionObject {

    companion object {
        private const val TAG = "DimensionObject"
    }

    internal var model = FloatArray(16)

    init {
        setIdentityM(
            model,
            0
        )
    }

    open fun setPosition(
        v: Vector
    ) {
        setPosition(
            v.x,
            v.y,
            v.z
        )
    }

    open fun setPosition(
        x: Float,
        y: Float,
        z: Float
    ) {
        model[12] = x
        model[13] = y
        model[14] = z
    }

    open fun setScale(
        v: Vector
    ) {
        setScale(
            v.x,
            v.y,
            v.z
        )
    }

    open fun setScale(
        x: Float,
        y: Float,
        z: Float
    ) {
        scaleM(
            model,
            0,
            x,
            y,
            z
        )
    }
}
