package good.damn.logic.triggers

import good.damn.common.matrices.COMatrixScaleRotation

interface LGITrigger {
    val modelMatrix: COMatrixScaleRotation
    fun trigger(
        position4: FloatArray
    )
}