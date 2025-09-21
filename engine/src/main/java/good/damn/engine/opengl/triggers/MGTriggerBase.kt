package good.damn.engine.opengl.triggers

import android.opengl.Matrix
import android.util.Log
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.camera.MGMMatrix

abstract class MGTriggerBase(
    private val triggerMethod: MGITriggerMethod,
    private val modelMatrix: MGMMatrix
) {

    private var mIsInside = false
    private val mTransformedPosition = FloatArray(4)
    private val mTriggerPosition = FloatArray(4)

    fun trigger(
        x: Float,
        y: Float,
        z: Float
    ) {
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

        val xx = mTransformedPosition[0]
        val yy = mTransformedPosition[1]
        val zz = mTransformedPosition[2]

        if (mIsInside) {
            if (!triggerMethod.canTrigger(
                xx, yy, zz
            )) {
                mIsInside = false
                onTriggerEnd()
            }
            return
        }

        if (triggerMethod.canTrigger(
            xx, yy, zz
        )) {
            mIsInside = true
            onTriggerBegin()
        }

    }

    abstract fun onTriggerBegin()
    abstract fun onTriggerEnd()

}