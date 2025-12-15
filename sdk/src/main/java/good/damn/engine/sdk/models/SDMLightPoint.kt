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
    companion object {
        @JvmField
        val COLOR_DIVISOR = 256f / 5f
    }

    val radius: Float
        get() = (
            -linear + sqrt(
                linear * linear - 4 * quad * (
                    constant - COLOR_DIVISOR * color.maxValue()
                )
            )
        ) / (2 * quad)
}