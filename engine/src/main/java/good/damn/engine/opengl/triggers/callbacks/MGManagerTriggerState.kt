package good.damn.engine.opengl.triggers.callbacks

import good.damn.engine.opengl.enums.MGEnumStateTrigger
import good.damn.engine.opengl.triggers.methods.MGITriggerMethod

class MGManagerTriggerState(
    private val triggerMethod: MGITriggerMethod
) {
    private var mIsInside = false

    fun trigger(
        position4: FloatArray
    ): MGEnumStateTrigger {
        if (mIsInside) {
            if (!triggerMethod.canTrigger(
                position4
            )) {
                mIsInside = false
                return MGEnumStateTrigger.END
            }
            return MGEnumStateTrigger.MOVE
        }

        if (triggerMethod.canTrigger(
           position4
        )) {
            mIsInside = true
            return MGEnumStateTrigger.BEGIN
        }

        return MGEnumStateTrigger.NONE
    }
}