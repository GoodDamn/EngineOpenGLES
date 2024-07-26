package good.damn.opengles_engine.views.touchable

import androidx.annotation.ColorInt

data class Axis(
    @ColorInt val color: Int,
    val onMove: (Float) -> Unit
)