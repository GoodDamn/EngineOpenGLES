package good.damn.engine.touch

import android.view.MotionEvent

class MGTouchMove
: MGTouchBound() {

    companion object {
        private const val TAG = "MGTouchMove"
    }

    private var onMove: MGIListenerMove? = null

    fun setListenerMove(
        l: MGIListenerMove?
    ) { onMove = l }

    override fun onTouchDown(
        event: MotionEvent,
        touchIndex: Int
    ) = !event.isNotInsideBounds(
        mLeft, mTop,
        mRight, mBottom,
        touchIndex
    )

    override fun onTouchMove(
        event: MotionEvent,
        touchIndex: Int
    ) {
        val xx = event.getX(
            touchIndex
        )

        val yy = event.getY(
            touchIndex
        )

        val x = (when {
            xx < mLeft -> mLeft
            xx > mRight -> mRight
            else -> xx
        } - mHalfX) / mHalfBound

        val y = (when {
            yy < mTop -> mTop
            yy > mBottom -> mBottom
            else -> yy
        } - mHalfY) / mHalfBound

        onMove?.onMove(
            x, y
        )
    }
}