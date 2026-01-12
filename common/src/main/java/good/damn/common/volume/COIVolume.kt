package good.damn.common.volume

import good.damn.common.matrices.COMatrixModel

interface COIVolume {
    val modelMatrix: COMatrixModel

    fun isOnFrustrum(
        v: Boolean
    )
}