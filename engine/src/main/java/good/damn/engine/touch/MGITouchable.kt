package good.damn.engine.touch

import android.view.MotionEvent

interface MGITouchable {

    fun onTouchEvent(
        event: MotionEvent
    ): Boolean
}