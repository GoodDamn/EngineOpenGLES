package good.damn.engine.sdk.models

import good.damn.engine.sdk.SDVector3
import kotlin.math.sqrt

data class SDMLightPointEntity(
    val position: SDVector3,
    val radiusTrigger: Float,
    val light: SDMLightPoint
)