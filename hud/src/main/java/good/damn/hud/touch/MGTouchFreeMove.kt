package good.damn.hud.touch

import android.view.MotionEvent

class MGTouchFreeMove
: UIITouchable {

    private val mTouchMove = UITouchMove()
    private val mTouchDelta = UITouchDelta()

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
        l: UIIListenerDelta?
    ) {
        mTouchDelta.onDelta = l
    }

    fun setListenerMove(
        l: UIIListenerMove?
    ) = mTouchMove.setListenerMove(l)

    override fun onTouchEvent(
        event: MotionEvent
    ): Boolean {
        mTouchMove.onTouchEvent(
            event
        )

        mTouchDelta.onTouchEvent(
            event
        )

        return true
    }
}