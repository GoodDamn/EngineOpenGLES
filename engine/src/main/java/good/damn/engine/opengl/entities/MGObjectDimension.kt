package good.damn.engine.opengl.entities

import android.opengl.Matrix.*
import good.damn.engine.opengl.MGVector

open class MGObjectDimension {

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
        v: MGVector
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

    fun setPositionBy(
        dx: Float,
        dy: Float,
        dz: Float
    ) {
        model[12] += dx
        model[13] += dy
        model[14] += dz
    }

    open fun setScale(
        v: MGVector
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
