package good.damn.logic.triggers.stateables

import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.logic.triggers.callbacks.LGManagerTriggerStateCallback

data class LGTriggerStateable(
    val stateManager: LGManagerTriggerStateCallback,
    val modelMatrix: COMatrixScaleRotation
)