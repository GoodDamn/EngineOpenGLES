package good.damn.common.volume

import good.damn.engine.sdk.matrices.COMatrixModel

interface COIVolume {
    val modelMatrix: COMatrixModel

    fun isOnFrustrum(
        v: Boolean
    )
}