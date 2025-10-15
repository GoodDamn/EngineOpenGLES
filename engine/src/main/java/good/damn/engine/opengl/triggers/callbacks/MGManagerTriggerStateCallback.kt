package good.damn.engine.opengl.triggers.callbacks

import good.damn.engine.opengl.triggers.MGITrigger
import good.damn.engine.opengl.triggers.methods.MGITriggerMethod

class MGManagerTriggerStateCallback(
    private val triggerMethod: MGITriggerMethod,
    private val triggerCallback: MGITrigger,
) {
    private var mIsInside = false

    fun trigger(
        x: Float,
        y: Float,
        z: Float
    ) {
        if (mIsInside) {
            if (!triggerMethod.canTrigger(
                x, y, z
            )) {
                mIsInside = false
                triggerCallback.onTriggerEnd()
                return
            }
            return
        }

        if (triggerMethod.canTrigger(
            x, y, z
        )) {
            mIsInside = true
            triggerCallback.onTriggerBegin()
            return
        }

        return
    }
}