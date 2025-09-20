package good.damn.engine.opengl.triggers

import good.damn.engine.opengl.MGVector

abstract class MGTriggerBox(
    private val min: MGVector,
    private val max: MGVector
): MGITrigger {
    final override fun canTrigger(
        x: Float,
        y: Float,
        z: Float
    ) = !(min.x > x || max.x < x ||
        min.y > y || max.y < y ||
        min.z > z || max.z < z)
}