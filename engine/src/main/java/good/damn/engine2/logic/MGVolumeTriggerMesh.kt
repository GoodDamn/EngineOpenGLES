package good.damn.engine2.logic

import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.volume.COIVolume
import good.damn.logic.triggers.LGITrigger

data class MGVolumeTriggerMesh(
    override val modelMatrix: COMatrixScaleRotation,
    val geometryFrustrum: MGIGeometryFrustrum
): LGITrigger,
COIVolume {

    override fun trigger(
        position4: FloatArray
    ) = Unit

    override fun isOnFrustrum(
        v: Boolean
    ) {
        geometryFrustrum.isOn = v
    }
}