package good.damn.logic.triggers.methods

import good.damn.engine.sdk.SDVector3
import good.damn.common.matrices.COMatrixInvert

class LGTriggerMethodBox(
    modelMatrix: COMatrixInvert
): LGTriggerMethodInvert(
    modelMatrix
) {
    companion object {
        private val MIN = SDVector3(
            -0.5f, -0.5f, -0.5f
        )
        private val MAX = SDVector3(
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