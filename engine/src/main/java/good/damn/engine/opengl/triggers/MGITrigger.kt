package good.damn.engine.opengl.triggers

interface MGITrigger {
    fun canTrigger(
        x: Float,
        y: Float,
        z: Float
    ): Boolean
}