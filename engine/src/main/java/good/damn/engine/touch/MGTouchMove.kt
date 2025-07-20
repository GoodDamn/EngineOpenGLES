package good.damn.engine.touch

import android.view.MotionEvent

class MGTouchMove
: MGITouchable {

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

    override fun onTouchEvent(
        event: MotionEvent
    ) {
        if (event.isNotInsideBounds(
            mLeft, mTop,
            mRight, mBottom
        )) {
            return
        }

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val x = (when {
                    event.x < mLeft -> mLeft
                    event.x > mRight -> mRight
                    else -> event.x
                } - mHalfX) / mHalfBound

                val y = (when {
                    event.y < mTop -> mTop
                    event.y > mBottom -> mBottom
                    else -> event.y
                } - mHalfY) / mHalfBound

                onMove?.onMove(
                    x, y
                )

            }
        }
        
    }
    
}