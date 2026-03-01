package good.damn.engine.sdk.trigger.methods

interface LGITriggerMethod {
    fun canTrigger(
        position4: FloatArray
    ): Boolean
}