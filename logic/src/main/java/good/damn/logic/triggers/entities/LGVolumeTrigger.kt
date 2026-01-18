package good.damn.logic.triggers.entities

import good.damn.common.matrices.COMatrixModel
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.volume.COIVolume
import good.damn.logic.triggers.callbacks.LGManagerTriggerState
import good.damn.logic.triggers.callbacks.LGManagerTriggerStateCallback

class LGVolumeTrigger(
    override val modelMatrix: COMatrixScaleRotation,
    val stateManager: LGManagerTriggerStateCallback
): COIVolume {

    override fun isOnFrustrum(
        v: Boolean
    ) {

    }
}