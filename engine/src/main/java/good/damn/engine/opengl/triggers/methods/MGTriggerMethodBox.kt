package good.damn.engine.opengl.triggers.methods

import good.damn.engine.sdk.SDVector3
import good.damn.common.matrices.COMatrixInvert

class MGTriggerMethodBox(
    modelMatrix: COMatrixInvert
): MGTriggerMethodInvert(
    modelMatrix
) {
    companion object {
        val MIN = SDVector3(
            -0.5f, -0.5f, -0.5f
        )
        val MAX = SDVector3(
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