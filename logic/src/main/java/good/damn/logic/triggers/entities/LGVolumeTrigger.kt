package good.damn.logic.triggers.entities

import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.logic.triggers.callbacks.LGIManagerTriggerState
import good.damn.logic.triggers.callbacks.LGManagerTriggerStateCallback

data class LGVolumeTrigger(
    override val modelMatrix: COMatrixScaleRotation,
    override val state: LGManagerTriggerStateCallback,
): LGIManagerTriggerState