package good.damn.engine.touch

import android.view.MotionEvent

class MGTouchFreeMove
: MGITouchable {

    private val mTouchMove = MGTouchMove()
    private val mTouchDelta = MGTouchDelta()

    fun setBoundsMove(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) = mTouchMove.setBounds(
        left, top,
        right, bottom
    )

    fun setBoundsDelta(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) = mTouchDelta.setBounds(
        left, top,
        right, bottom
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