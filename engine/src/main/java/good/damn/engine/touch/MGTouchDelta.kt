package good.damn.engine.touch

import android.view.InputDevice.MotionRange
import android.view.MotionEvent

class MGTouchDelta
: MGITouchable {

    var onDelta: MGIListenerDelta? = null

    private var mHalfBound = 0f

    private var mHalfX = 0f
    private var mHalfY = 0f

    private var mLeft = 0f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = 0f

    private var mIndexTouch = -1

    private var mPrevX = 0f
    private var mPrevY = 0f

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
        if (event.pointerCount > 2) {
            return
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                processDown(
                    0,
                    event
                )
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                processDown(
                    1,
                    event
                )
            }

            MotionEvent.ACTION_MOVE -> {
                if (mIndexTouch == -1) {
                    return
                }

                val x = event.getX(mIndexTouch)
                val y = event.getY(mIndexTouch)

                val dx = x - mPrevX
                val dy = mPrevY - y

                onDelta?.onDelta(
                    dx, dy
                )

                mPrevX = x
                mPrevY = y
            }

            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> {
                mIndexTouch = -1
            }
        }
    }

    private inline fun processDown(
        index: Int,
        event: MotionEvent
    ) {
        mPrevX = event.getX(index)
        mPrevY = event.getY(index)
        mIndexTouch = index

        if (event.isNotInsideBounds(
            mLeft, mTop,
            mRight, mBottom,
            mIndexTouch
        )) {
            mIndexTouch = -1
            return
        }
    }
}