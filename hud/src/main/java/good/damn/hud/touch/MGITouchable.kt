package good.damn.hud.touch

import android.view.MotionEvent

interface MGITouchable {

    fun onTouchEvent(
        event: MotionEvent
    ): Boolean
}