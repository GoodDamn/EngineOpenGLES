package good.damn.engine2.logic

import good.damn.engine.sdk.matrices.SDMatrixScaleRotation
import good.damn.common.volume.COIVolume
import good.damn.engine.sdk.trigger.LGITrigger

data class MGVolumeTriggerMesh(
    override val modelMatrix: SDMatrixScaleRotation,
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