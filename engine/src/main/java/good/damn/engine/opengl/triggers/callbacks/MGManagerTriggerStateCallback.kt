package good.damn.engine.opengl.triggers.callbacks

import good.damn.engine.opengl.triggers.MGITrigger
import good.damn.engine.opengl.triggers.methods.MGITriggerMethod

class MGManagerTriggerStateCallback(
    private val triggerMethod: MGITriggerMethod,
    private val triggerCallback: MGITrigger,
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