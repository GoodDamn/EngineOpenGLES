package good.damn.engine.opengl.entities

import android.opengl.Matrix
import android.opengl.Matrix.*
import good.damn.engine.opengl.MGVector

open class MGObjectDimension {

    companion object {
        private const val TAG = "DimensionObject"
        const val INDEX_X = 12
        const val INDEX_Y = 13
        const val INDEX_Z = 14
    }

    internal var model = FloatArray(16)

    init {
        setIdentityM(
            model,
            0
        )
    }

    protected fun invalidatePosition(
        centerX: Float,
        centerY: Float,
        centerZ: Float
    ) {
        setLookAtM(
            model,
            0,
            model[INDEX_X], model[INDEX_Y], model[INDEX_Z],
            centerX, centerY, centerZ,
            0.0f, 1.0f, 0.0f
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
        model[INDEX_X] = x
        model[INDEX_Y] = y
        model[INDEX_Z] = z
    }

    open fun addPosition(
        dx: Float,
        dy: Float,
        dz: Float
    ) {
        model[INDEX_X] += dx
        model[INDEX_Y] += dy
        model[INDEX_Z] += dz
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
