package good.damn.engine.sdk.trigger

import good.damn.engine.sdk.matrices.SDMatrixScaleRotation

interface LGITrigger {
    val modelMatrix: SDMatrixScaleRotation
    fun trigger(
        position4: FloatArray
    )
}