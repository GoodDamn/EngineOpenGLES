package good.damn.engine.opengl.triggers.callbacks

import good.damn.engine.opengl.enums.MGEnumStateTrigger
import good.damn.engine.opengl.triggers.methods.MGITriggerMethod

class MGManagerTriggerState(
    private val triggerMethod: MGITriggerMethod
) {
    private var mIsInside = false

    private val mTransformedPosition = floatArrayOf(
        0.0f,
        0.0f,
        0.0f,
        0.0f
    )

    fun trigger(
        x: Float,
        y: Float,
        z: Float
    ): MGEnumStateTrigger {
        mTransformedPosition[0] = x;
        mTransformedPosition[1] = y;
        mTransformedPosition[2] = z;

        if (mIsInside) {
            if (!triggerMethod.canTrigger(
                mTransformedPosition
            )) {
                mIsInside = false
                return MGEnumStateTrigger.END
            }
            return MGEnumStateTrigger.MOVE
        }

        if (triggerMethod.canTrigger(
           mTransformedPosition
        )) {
            mIsInside = true
            return MGEnumStateTrigger.BEGIN
        }

        return MGEnumStateTrigger.NONE
    }
}