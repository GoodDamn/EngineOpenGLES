package good.damn.engine.opengl.triggers

interface MGITriggerMethod {
    fun canTrigger(
        x: Float,
        y: Float,
        z: Float
    ): Boolean
}