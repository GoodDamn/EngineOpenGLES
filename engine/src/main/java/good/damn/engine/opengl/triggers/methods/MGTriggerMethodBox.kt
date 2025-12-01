package good.damn.engine.opengl.triggers.methods

import good.damn.engine.sdk.MGVector3
import good.damn.engine.opengl.matrices.MGMatrixInvert

class MGTriggerMethodBox(
    modelMatrix: MGMatrixInvert
): MGTriggerMethodInvert(
    modelMatrix
) {
    companion object {
        val MIN = MGVector3(
            -0.5f, -0.5f, -0.5f
        )
        val MAX = MGVector3(
            0.5f, 0.5f, 0.5f
        )
    }

    override fun canTriggerTransformed(
        x: Float,
        y: Float,
        z: Float
    ) = !(MIN.x > x || MAX.x < x ||
        MIN.y > y || MAX.y < y ||
        MIN.z > z || MAX.z < z)
}