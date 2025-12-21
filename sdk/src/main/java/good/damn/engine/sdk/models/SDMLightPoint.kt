package good.damn.engine.sdk.models

import good.damn.engine.sdk.SDVector3
import kotlin.math.sqrt

data class SDMLightPoint(
    var color: SDVector3,
    var interpolation: SDMLightPointInterpolation,
    var alpha: Float = 1.0f
) {
    companion object {
        @JvmField
        val COLOR_DIVISOR = 256f / 5f
    }

    val radius: Float
        get() = interpolation.run {
            (-linear + sqrt(
                    linear * linear - 4 * quad * (
                        constant - COLOR_DIVISOR * color.maxValue()
                    )
                )
            ) / (2 * quad)
        }
}