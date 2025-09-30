package good.damn.engine.opengl.triggers.methods

interface MGITriggerMethod {
    fun canTrigger(
        x: Float,
        y: Float,
        z: Float
    ): Boolean
}