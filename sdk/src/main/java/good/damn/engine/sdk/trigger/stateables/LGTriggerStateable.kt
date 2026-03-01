package good.damn.engine.sdk.trigger.stateables

import good.damn.engine.sdk.trigger.enums.LGEnumStateTrigger
import good.damn.engine.sdk.trigger.methods.LGITriggerMethod


class LGTriggerStateable(
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