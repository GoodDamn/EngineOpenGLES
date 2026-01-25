package good.damn.logic.triggers.callbacks

import good.damn.common.matrices.COMatrixScaleRotation

interface LGIManagerTriggerState {
    val modelMatrix: COMatrixScaleRotation
    val state: LGManagerTriggerStateCallback
}