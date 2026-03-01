package good.damn.logic.triggers

import good.damn.engine.sdk.matrices.SDMatrixScaleRotation

interface LGITrigger {
    val modelMatrix: SDMatrixScaleRotation
    fun trigger(
        position4: FloatArray
    )
}