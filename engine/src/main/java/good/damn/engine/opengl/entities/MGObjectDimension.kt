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

    var x = 0f
    var y = 0f
    var z = 0f

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
        this.x = x
        this.y = y
        this.z = z
    }

    open fun addPosition(
        dx: Float,
        dy: Float,
        dz: Float
    ) {
        x += dx
        y += dy
        z += dz
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
