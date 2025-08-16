package good.damn.engine.opengl.camera

import android.opengl.Matrix
import android.opengl.Matrix.setLookAtM

class MGMMatrix {

    companion object {
        private const val INDEX_X = 12
        private const val INDEX_Y = 13
        private const val INDEX_Z = 14
    }

    var model = FloatArray(16)

    var x = 0f
    var y = 0f
    var z = 0f

    init {
        Matrix.setIdentityM(
            model,
            0
        )
    }


    fun invalidatePosition() {
        model[INDEX_X] = x
        model[INDEX_Y] = y
        model[INDEX_Z] = z
    }

    fun addPosition(
        dx: Float,
        dy: Float,
        dz: Float
    ) {
        x += dx
        y += dy
        z += dz
    }

    fun setScale(
        x: Float,
        y: Float,
        z: Float
    ) {
        Matrix.scaleM(
            model,
            0,
            x, y, z
        )
    }
}