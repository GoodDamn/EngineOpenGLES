package good.damn.common.volume

import good.damn.common.matrices.MGMatrixModel

interface COIVolume {
    val modelMatrix: MGMatrixModel

    fun isOnFrustrum(
        v: Boolean
    )
}