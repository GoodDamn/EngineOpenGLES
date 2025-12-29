package good.damn.engine.sdk.models

import kotlin.math.sqrt

data class SDMLightPointInterpolation(
    var constant: Float,
    var linear: Float,
    var quad: Float,
    var radiusClip: Float
)