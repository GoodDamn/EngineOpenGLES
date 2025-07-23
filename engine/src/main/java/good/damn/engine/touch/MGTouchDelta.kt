package good.damn.engine.touch

import android.util.Log
import android.view.InputDevice.MotionRange
import android.view.MotionEvent

class MGTouchDelta
: MGITouchable {

    companion object {
        private const val TAG = "MGTouchDelta"
    }

    var onDelta: MGIListenerDelta? = null

    private var mHalfBound = 0f

    private var mHalfX = 0f
    private var mHalfY = 0f

    private var mLeft = 0f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = 0f

    private var mTouchId = -1

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
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {

                val index = event.actionIndex
                Log.d(TAG, "onTouchEvent: DOWN: $mTouchId:: ${event.actionIndex}")
                if (mTouchId != -1) {
                    return
                }
                mPrevX = event.getX(index)
                mPrevY = event.getY(index)
                mTouchId = event.getPointerId(
                    index
                )

                if (event.isNotInsideBounds(
                        mLeft, mTop,
                        mRight, mBottom,
                        index
                    )) {
                    mTouchId = -1
                    return
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (mTouchId == -1) {
                    return
                }

                val mIndexTouch = event.findPointerIndex(
                    mTouchId
                )

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

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_UP -> {
                Log.d(TAG, "onTouchEvent: UP: ${event.findPointerIndex(mTouchId)}:::${event.actionIndex}")
                if (event.findPointerIndex(mTouchId) == event.actionIndex) {
                    mTouchId = -1
                }
            }

            MotionEvent.ACTION_UP -> {
                mTouchId = -1
            }
        }
    }
}