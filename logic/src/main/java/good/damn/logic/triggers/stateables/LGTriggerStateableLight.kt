package good.damn.logic.triggers.stateables

import good.damn.common.matrices.COMatrixScale
import good.damn.common.matrices.COMatrixTransformationInvert
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.logic.triggers.LGMatrixTriggerLight
import good.damn.logic.triggers.callbacks.LGManagerTriggerState
import good.damn.logic.triggers.methods.LGTriggerMethodSphere

data class LGTriggerStateableLight(
    val light: SDMLightPoint,
    val modelMatrix: LGMatrixTriggerLight
) {
    companion object {
        @JvmStatic
        fun createFromLight(
            light: SDMLightPoint
        ) = LGTriggerStateableLight(
            light,
            LGMatrixTriggerLight(
                COMatrixTransformationInvert(
                    COMatrixScale()
                )
            )
        )
    }
}