package good.damn.engine.touch

import android.view.MotionEvent

class MGTouchFreeMove
: MGITouchable {

    private val mTouchMove = MGTouchMove()
    private val mTouchDelta = MGTouchDelta()

    fun setBoundsMove(
        left: Float,
        top: Float,
        s: Float
    ) = mTouchMove.setBounds(
        left,
        top, s
    )

    fun setListenerDelta(
        l: MGIListenerDelta?
    ) {
        mTouchDelta.onDelta = l
    }

    fun setListenerMove(
        l: MGIListenerMove?
    ) = mTouchMove.setListenerMove(l)

    override fun onTouchEvent(
        event: MotionEvent
    ) {
        mTouchMove.onTouchEvent(
            event
        )

        mTouchDelta.onTouchEvent(
            event
        )
    }
}