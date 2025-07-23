package good.damn.engine.touch

import android.util.Log
import android.view.MotionEvent

class MGTouchMove
: MGTouchMulti() {

    companion object {
        private const val TAG = "MGTouchMove"
    }

    private var onMove: MGIListenerMove? = null

    private var mHalfBound = 0f

    private var mHalfX = 0f
    private var mHalfY = 0f

    private var mLeft = 0f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = 0f

    fun setListenerMove(
        l: MGIListenerMove?
    ) { onMove = l }

    fun setBounds(
        left: Float,
        top: Float,
        s: Float
    ) {
        mLeft = left
        mTop = top
        mRight = left + s
        mBottom = top + s
        mHalfBound = s * 0.5f
        mHalfX = (mRight + mLeft) * 0.5f
        mHalfY = (mBottom + mTop) * 0.5f
    }

    override fun onTouchDown(
        event: MotionEvent,
        touchIndex: Int
    ) = event.isNotInsideBounds(
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