package good.damn.engine.opengl.entities

import good.damn.engine.sdk.MGVector3
import kotlin.math.sqrt

data class MGLight(
    var color: MGVector3,
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