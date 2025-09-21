package good.damn.engine.opengl.camera

import android.opengl.Matrix

class MGMMatrix {

    companion object {
        private const val INDEX_X = 12
        private const val INDEX_Y = 13
        private const val INDEX_Z = 14
    }

    var model = FloatArray(16)
        private set

    var normalMatrix = FloatArray(16)
        private set

    var modelInverted = FloatArray(16)
        private set

    var x = 0f
    var y = 0f
    var z = 0f

    init {
        Matrix.setIdentityM(
            model,
            0
        )

        Matrix.setIdentityM(
            normalMatrix,
            0
        )
    }


    fun invalidatePosition() {
        model[INDEX_X] = x
        model[INDEX_Y] = y
        model[INDEX_Z] = z
    }

    fun addRotation(
        yaw: Float,
        pitch: Float
    ) {
        Matrix.rotateM(
            model,
            0,
            yaw,
            0.0f,
            1.0f,
            0.0f
        )
        calculateNormals()
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
        calculateNormals()
    }

    private inline fun calculateNormals() {
        Matrix.invertM(
            modelInverted,
            0,
            model,
            0
        )

        Matrix.transposeM(
            normalMatrix,
            0,
            modelInverted,
            0
        )
    }
}