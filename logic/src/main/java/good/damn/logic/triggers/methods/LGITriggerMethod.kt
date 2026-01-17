package good.damn.logic.triggers.methods

interface LGITriggerMethod {
    fun canTrigger(
        position4: FloatArray
    ): Boolean
}