package good.damn.engine.touch

import android.view.MotionEvent

class MGTouchFreeMove {

    private val mTouchMove = MGTouchMove()

    fun setListenerMove(
        l: MGIListenerMove?
    ) = mTouchMove.setListenerMove(l)

    fun touchEvent(
        event: MotionEvent
    ) {
        mTouchMove.onTouchEvent(
            event
        )
    }
}