package good.damn.engine.opengl.camera

import android.opengl.Matrix

class MGMMatrix {

    companion object {
        private const val INDEX_X = 12
        private const val INDEX_Y = 13
        private const val INDEX_Z = 14
    }

    val model = FloatArray(16)
    val normalMatrix = FloatArray(16)
    val modelInverted = FloatArray(16)

    val x: Float
        get() = matrixTranslate[INDEX_X]

    val y: Float
        get() = matrixTranslate[INDEX_Y]

    val z: Float
        get() = matrixTranslate[INDEX_Z]

    private val matrixRotation = FloatArray(16)
    private val matrixScale = FloatArray(16)
    private val matrixTranslate = FloatArray(16)

    init {
        Matrix.setIdentityM(
            model,
            0
        )

        Matrix.setIdentityM(
            normalMatrix,
            0
        )

        Matrix.setIdentityM(
            matrixRotation,
            0
        )

        Matrix.setIdentityM(
            matrixScale,
            0
        )
        Matrix.setIdentityM(
            matrixTranslate,
            0
        )
    }

    fun invalidateTransform() {
        Matrix.setIdentityM(
            model,
            0
        )

        Matrix.multiplyMM(
            model,
            0,
            matrixScale,
            0,
            model,
            0
        )

        Matrix.multiplyMM(
            model,
            0,
            matrixRotation,
            0,
            model,
            0
        )

        Matrix.multiplyMM(
            model,
            0,
            matrixTranslate,
            0,
            model,
            0
        )
    }

    fun addRotation(
        dYaw: Float,
        pitch: Float
    ) {
        Matrix.rotateM(
            matrixRotation,
            0,
            dYaw,
            0.0f,
            1.0f,
            0.0f
        )
        calculateNormals()
    }

    fun setPosition(
        x: Float,
        y: Float,
        z: Float
    ) {
        Matrix.setIdentityM(
            matrixTranslate,
            0
        )

        Matrix.translateM(
            matrixTranslate,
            0,
            x, y, z
        )
    }

    fun addPosition(
        dx: Float,
        dy: Float,
        dz: Float
    ) {
        Matrix.translateM(
            matrixTranslate,
            0,
            dx, dy, dz
        )
    }

    fun setScale(
        x: Float,
        y: Float,
        z: Float
    ) {
        Matrix.scaleM(
            matrixScale,
            0,
            x, y, z
        )
        calculateNormals()
    }

    private fun calculateNormals() {
        Matrix.setIdentityM(
            modelInverted,
            0
        )

        Matrix.multiplyMM(
            modelInverted,
            0,
            matrixScale,
            0,
            modelInverted,
            0
        )

        Matrix.multiplyMM(
            modelInverted,
            0,
            matrixRotation,
            0,
            modelInverted,
            0
        )

        Matrix.invertM(
            modelInverted,
            0,
            modelInverted,
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