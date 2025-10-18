package good.damn.engine.opengl.triggers.methods

interface MGITriggerMethod {
    fun canTrigger(
        position4: FloatArray
    ): Boolean
}