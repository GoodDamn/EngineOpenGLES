package good.damn.engine.opengl.triggers.callbacks

import good.damn.engine.opengl.enums.MGEnumStateTrigger
import good.damn.engine.opengl.triggers.methods.MGITriggerMethod

class MGManagerTriggerState(
    private val triggerMethod: MGITriggerMethod
) {
    private var mIsInside = false

    fun trigger(
        x: Float,
        y: Float,
        z: Float
    ): MGEnumStateTrigger {
        if (mIsInside) {
            if (!triggerMethod.canTrigger(
                x, y, z
            )) {
                mIsInside = false
                return MGEnumStateTrigger.END
            }
            return MGEnumStateTrigger.MOVE
        }

        if (triggerMethod.canTrigger(
            x, y, z
        )) {
            mIsInside = true
            return MGEnumStateTrigger.BEGIN
        }

        return MGEnumStateTrigger.NONE
    }
}