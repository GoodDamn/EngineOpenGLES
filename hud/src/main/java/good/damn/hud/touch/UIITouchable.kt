package good.damn.hud.touch

import android.view.MotionEvent

interface UIITouchable {

    fun onTouchEvent(
        event: MotionEvent
    ): Boolean
}