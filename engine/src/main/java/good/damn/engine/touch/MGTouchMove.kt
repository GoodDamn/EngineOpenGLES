package good.damn.engine.touch

import android.util.Log
import android.view.MotionEvent

class MGTouchMove
: MGITouchable {

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

    private var mTouchId = -1

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
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                Log.d(TAG, "onTouchEvent: DOWN: $mTouchId::$index")
                if (mTouchId != -1) {
                    return
                }
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

                val mTouchIndex = event.findPointerIndex(
                    mTouchId
                )

                val xx = event.getX(
                    mTouchIndex
                )

                val yy = event.getY(
                    mTouchIndex
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