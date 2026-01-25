package good.damn.engine2.logic

import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.volume.COIVolume
import good.damn.logic.triggers.callbacks.LGIManagerTriggerState
import good.damn.logic.triggers.callbacks.LGManagerTriggerStateCallback

data class MGVolumeTriggerMesh(
    override val modelMatrix: COMatrixScaleRotation,
    override val state: LGManagerTriggerStateCallback,
    val geometryFrustrum: MGMGeometryFrustrumMesh
): LGIManagerTriggerState,
COIVolume {
    override fun isOnFrustrum(
        v: Boolean
    ) {
        geometryFrustrum.isOn = v
    }
}