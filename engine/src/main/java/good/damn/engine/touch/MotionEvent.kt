package good.damn.engine.touch

import android.view.MotionEvent

inline fun MotionEvent.isNotInsideBounds(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float
) = x < left || y < top || x > right || y > bottom