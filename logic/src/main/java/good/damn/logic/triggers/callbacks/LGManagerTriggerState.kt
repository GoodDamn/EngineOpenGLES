package good.damn.logic.triggers.callbacks

import good.damn.logic.triggers.enums.LGEnumStateTrigger
import good.damn.logic.triggers.methods.LGITriggerMethod

class LGManagerTriggerState(
    private val triggerMethod: LGITriggerMethod
) {
    private var mIsInside = false

    fun trigger(
        position4: FloatArray
    ): LGEnumStateTrigger {
        if (mIsInside) {
            if (!triggerMethod.canTrigger(
                position4
            )) {
                mIsInside = false
                return LGEnumStateTrigger.END
            }
            return LGEnumStateTrigger.MOVE
        }

        if (triggerMethod.canTrigger(
           position4
        )) {
            mIsInside = true
            return LGEnumStateTrigger.BEGIN
        }

        return LGEnumStateTrigger.NONE
    }
}