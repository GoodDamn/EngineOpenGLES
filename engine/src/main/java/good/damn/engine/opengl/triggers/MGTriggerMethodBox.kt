package good.damn.engine.opengl.triggers

import good.damn.engine.opengl.MGVector

class MGTriggerMethodBox(
    private val min: MGVector,
    private val max: MGVector
): MGITriggerMethod {
    override fun canTrigger(
        x: Float,
        y: Float,
        z: Float
    ) = !(min.x > x || max.x < x ||
        min.y > y || max.y < y ||
        min.z > z || max.z < z)
}