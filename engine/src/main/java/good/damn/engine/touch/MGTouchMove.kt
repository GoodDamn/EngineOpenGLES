package good.damn.engine.touch

import android.text.BoringLayout
import android.view.MotionEvent

class MGTouchMove
: MGTouchBound() {

    companion object {
        private const val TAG = "MGTouchMove"
    }

    private var onMove: MGIListenerMove? = null

    private var mAnchorX = 0f
    private var mAnchorY = 0f

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

        return true
    }

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

        val x = when {
            xx < mLeft -> mLeft
            xx > mRight -> mRight
            else -> xx
        } - mAnchorX

        val y = when {
            yy < mTop -> mTop
            yy > mBottom -> mBottom
            else -> yy
        } - mAnchorY

        onMove?.onMove(
            x, y
        )
    }
}