package good.damn.engine.opengl.triggers

import android.opengl.Matrix
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.matrices.MGMMatrix
import good.damn.engine.opengl.matrices.MGMatrixScale

class MGTriggerMethodBox(
    private val modelMatrix: MGMatrixScale
): MGITriggerMethod {

    companion object {
        val MIN = MGVector(
            -0.5f, -0.5f, -0.5f
        )
        val MAX = MGVector(
            0.5f, 0.5f, 0.5f
        )
    }

    private val mTransformedPosition = FloatArray(4)
    private val mTriggerPosition = FloatArray(4)

    override fun canTrigger(
        x: Float,
        y: Float,
        z: Float
    ): Boolean {
        mTriggerPosition[0] = x
        mTriggerPosition[1] = y
        mTriggerPosition[2] = z
        mTriggerPosition[3] = 0.0f

        Matrix.multiplyMV(
            mTransformedPosition,
            0,
            modelMatrix.modelInverted,
            0,
            mTriggerPosition,
            0
        )

        return canTriggerr(
            mTransformedPosition[0],
            mTransformedPosition[1],
            mTransformedPosition[2]
        )
    }


    private inline fun canTriggerr(
        x: Float,
        y: Float,
        z: Float
    ) = !(MIN.x > x || MAX.x < x ||
        MIN.y > y || MAX.y < y ||
        MIN.z > z || MAX.z < z)
}