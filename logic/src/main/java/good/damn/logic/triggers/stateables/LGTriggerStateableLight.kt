package good.damn.logic.triggers.stateables

import good.damn.engine.sdk.matrices.SDMatrixScale
import good.damn.engine.sdk.matrices.SDMatrixTransformationInvert
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.logic.triggers.LGMatrixTriggerLight

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
                SDMatrixTransformationInvert(
                    SDMatrixScale()
                )
            )
        )
    }
}