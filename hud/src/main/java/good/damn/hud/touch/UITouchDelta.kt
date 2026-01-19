package good.damn.hud.touch

import android.view.MotionEvent

class UITouchDelta
: UITouchBound() {

    companion object {
        private const val TAG = "MGTouchDelta"
    }

    var onDelta: UIIListenerDelta? = null

    private var mPrevX = 0f
    private var mPrevY = 0f

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
        val index = event.actionIndex
        mPrevX = event.getX(index)
        mPrevY = event.getY(index)
        return true
    }

    override fun onTouchMove(
        event: MotionEvent,
        touchIds: List<Int>
    ) {
        val touchIndex = event.findPointerIndex(
            touchIds[0]
        )

        val x = event.getX(
            touchIndex
        )

        val y = event.getY(
            touchIndex
        )

        val dx = x - mPrevX
        val dy = mPrevY - y

        onDelta?.onDelta(
            dx, dy
        )

        mPrevX = x
        mPrevY = y
    }
}