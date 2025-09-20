package good.damn.engine.opengl.triggers

abstract class MGTriggerBase(
    private val triggerMethod: MGITriggerMethod
) {

    private var isInside = false

    fun trigger(
        x: Float,
        y: Float,
        z: Float
    ) {
        if (isInside) {
            if (!triggerMethod.canTrigger(
                x, y, z
            )) {
                isInside = false
                onTriggerEnd()
            }
            return
        }

        if (triggerMethod.canTrigger(
            x, y, z
        )) {
            isInside = true
            onTriggerBegin()
        }

    }

    abstract fun onTriggerBegin()
    abstract fun onTriggerEnd()

}