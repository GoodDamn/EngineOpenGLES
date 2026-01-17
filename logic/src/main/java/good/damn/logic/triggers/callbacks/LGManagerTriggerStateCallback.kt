package good.damn.logic.triggers.callbacks

import good.damn.logic.triggers.LGITrigger
import good.damn.logic.triggers.methods.LGITriggerMethod


class LGManagerTriggerStateCallback(
    private val triggerMethod: LGITriggerMethod,
    private val triggerCallback: LGITrigger,
) {
    private var mIsInside = false

    fun trigger(
        position4: FloatArray
    ) {
        if (mIsInside) {
            if (!triggerMethod.canTrigger(
                position4
            )) {
                mIsInside = false
                triggerCallback.onTriggerEnd()
                return
            }
            return
        }

        if (triggerMethod.canTrigger(
            position4
        )) {
            mIsInside = true
            triggerCallback.onTriggerBegin()
            return
        }

        return
    }
}