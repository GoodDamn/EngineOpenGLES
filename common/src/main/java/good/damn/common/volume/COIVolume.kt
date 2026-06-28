package good.damn.common.volume

import good.damn.engine.sdk.matrices.SDMatrixModel

interface COIVolume {
    val modelMatrix: SDMatrixModel

    fun isOnFrustrum(
        v: Boolean
    )
}