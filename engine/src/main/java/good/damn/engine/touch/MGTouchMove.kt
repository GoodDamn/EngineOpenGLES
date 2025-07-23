package good.damn.engine.touch

import android.os.Handler
import android.os.Looper
import android.text.BoringLayout
import android.view.MotionEvent

class MGTouchMove
: MGTouchBound(),
Runnable {

    companion object {
        private const val TAG = "MGTouchMove"
        private const val DELAY_MOVE_MS = 16L
    }

    private var onMove: MGIListenerMove? = null

    private var mAnchorX = 0f
    private var mAnchorY = 0f

    private var mTouchX = 0f
    private var mTouchY = 0f

    private val mHandler = Handler(
        Looper.getMainLooper()
    )

    fun setListenerMove(
        l: MGIListenerMove?
    ) { onMove = l }

    override fun onTouchDown(
        event: MotionEvent,
        touchIndex: Int
    ): Boolean {
        if (event.isNotInsideBounds(
            mLeft, mTop,
            mRight, mBottom,
            touchIndex
        )) {
            return false
        }

        mAnchorX = event.getX(
            touchIndex
        )

        mAnchorY = event.getY(
            touchIndex
        )

        mTouchX = mAnchorX
        mTouchY = mAnchorY

        mHandler.post(
            this
        )
        return true
    }

    override fun onTouchMove(
        event: MotionEvent,
        touchIndex: Int
    ) {
        mTouchX = event.getX(
            touchIndex
        )

        mTouchY = event.getY(
            touchIndex
        )
    }

    override fun onTouchUp(
        event: MotionEvent,
        touchIndex: Int
    ) {
        mHandler.removeCallbacks(
            this
        )
    }

    override fun run() {
        val x = when {
            mTouchX < mLeft -> mLeft
            mTouchX > mRight -> mRight
            else -> mTouchX
        } - mAnchorX

        val y = when {
            mTouchY < mTop -> mTop
            mTouchY > mBottom -> mBottom
            else -> mTouchY
        } - mAnchorY

        onMove?.onMove(
            x, y
        )

        mHandler.postDelayed(
            this,
            DELAY_MOVE_MS
        )
    }
}