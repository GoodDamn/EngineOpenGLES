package good.damn.hud.touch

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import kotlin.math.min

class UITouchMove
: UITouchBound(),
Runnable {

    companion object {
        private const val TAG = "MGTouchMove"
        private const val DELAY_MOVE_MS = 16L
    }

    private var onMove: UIIListenerMove? = null

    private var mAnchorX = 0f
    private var mAnchorY = 0f

    private var mTouchX = 0f
    private var mTouchY = 0f

    private var mLimitDirection = 0f

    private val mHandler = Handler(
        Looper.getMainLooper()
    )

    override fun setBounds(
        left: Float, top: Float,
        right: Float, bottom: Float
    ) {
        super.setBounds(
            left, top,
            right, bottom
        )

        val fWidth = right - left
        val fHeight = bottom - top

        mLimitDirection = min(
            fWidth,
            fHeight
        ) * 0.5f
    }

    fun setListenerMove(
        l: UIIListenerMove?
    ) { onMove = l }

    override fun onTouchDown(
        event: MotionEvent,
        touchIndex: Int,
        touchIds: List<Int>
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
        touchIds: List<Int>
    ) {
        val touchIndex = event.findPointerIndex(
            touchIds[0]
        )

        mTouchX = event.getX(
            touchIndex
        )

        mTouchY = event.getY(
            touchIndex
        )
    }

    override fun onTouchUp(
        event: MotionEvent,
        touchIndex: Int,
        touchIds: List<Int>
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

        var directionX = x / mLimitDirection
        var directionY = y / mLimitDirection

        if (directionX > 1.0f) {
            directionX = 1.0f
        } else if (directionX < -1.0f) {
            directionX = -1.0f
        }

        if (directionY > 1.0f) {
            directionY = 1.0f
        } else if (directionY < -1.0f) {
            directionY = -1.0f
        }

        onMove?.onMove(
            x, y,
            directionX,
            directionY
        )

        mHandler.postDelayed(
            this,
            DELAY_MOVE_MS
        )
    }
}