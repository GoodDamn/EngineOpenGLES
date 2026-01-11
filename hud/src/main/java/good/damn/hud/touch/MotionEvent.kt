package good.damn.hud.touch

import android.view.MotionEvent

inline fun MotionEvent.isNotInsideBounds(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    index: Int = 0
) = getX(index) < left ||
    getY(index) < top ||
    getX(index) > right ||
    getY(index) > bottom