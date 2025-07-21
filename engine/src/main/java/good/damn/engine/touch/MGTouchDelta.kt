package good.damn.engine.touch

import android.view.MotionEvent

class MGTouchDelta
: MGITouchable {

    var onDelta: MGIListenerDelta? = null

    private var mHasTouch = false

    private var mHalfBound = 0f

    private var mHalfX = 0f
    private var mHalfY = 0f

    private var mLeft = 0f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = 0f

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

    private var mPrevX = 0f
    private var mPrevY = 0f

    override fun onTouchEvent(
        event: MotionEvent
    ) {
        if (event.isNotInsideBounds(
            mLeft, mTop,
            mRight, mBottom
        )) {
            return
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPrevX = event.x
                mPrevY = event.y

                mHasTouch = true
            }

            MotionEvent.ACTION_MOVE -> {
                if (!mHasTouch) {
                    return
                }

                val dx = event.x - mPrevX
                val dy = mPrevY - event.y

                onDelta?.onDelta(
                    dx, dy
                )

                mPrevX = event.x
                mPrevY = event.y
            }

            MotionEvent.ACTION_UP -> {
                mHasTouch = false
            }
        }
    }

    private fun processDown(
        event: MotionEvent
    ) {

    }
}