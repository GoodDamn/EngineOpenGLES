package good.damn.engine.sdk.models

import good.damn.engine.sdk.SDVector3
import kotlin.math.sqrt

data class SDMLightPoint(
    var color: SDVector3,
    var constant: Float,
    var linear: Float,
    var quad: Float,
    var radiusClip: Float
) {
    val radius: Float
        get() = (
            -linear + sqrt(
                linear * linear - 4 * quad * (
                    constant - (
                        256f / 5f
                        ) * color.maxValue()
                    )
            )
        ) / (2 * quad)
}