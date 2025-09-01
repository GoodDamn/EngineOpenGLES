package good.damn.engine.touch

import android.view.MotionEvent
import kotlin.math.hypot

class MGTouchScale
: MGTouchBound(
    maxTouches = 2
) {

    companion object {
        private const val SCALE_FACTOR = 0.01f
    }

    var onScale: MGIListenerScale? = null
    var onDelta: MGIListenerDelta? = null

    private var mPivotX = 0f
    private var mPivotY = 0f

    private var mPrevX = 0f
    private var mPrevY = 0f

    private var mPrevDistance = 0f
    private var mCurrentDistance = 0f

    private var mTranslateX = 0f
    private var mTranslateY = 0f

    private var mTranslate2X = 0f
    private var mTranslate2Y = 0f

    var scale = 1.0f
    private var mScaleDt = 0f

    private var mFirstTouchId = -1
    private var mSecondTouchId = -1

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

        if (mFirstTouchId == -1) {
            mPivotX = event.getX(
                touchIndex
            )

            mPivotY = event.getY(
                touchIndex
            )

            mPrevX = mPivotX
            mPrevY = mPivotY
            mFirstTouchId = event.getPointerId(
                touchIndex
            )
            return true
        }

        val firstIndex = event.findPointerIndex(
            mFirstTouchId
        )

        event.run {
            mPrevDistance = hypot(
                getX(touchIndex) - getX(firstIndex),
                getY(touchIndex) - getY(firstIndex)
            )
        }

        mSecondTouchId = event.getPointerId(
            touchIndex
        )
        return true
    }

    override fun onTouchMove(
        event: MotionEvent,
        touchIndex: Int
    ) {
        actionMove(
            event,
            touchIndex
        )
    }

    override fun onTouchUp(
        event: MotionEvent,
        touchIndex: Int
    ) {
        val localId = event.getPointerId(
            touchIndex
        )

        if (mFirstTouchId == localId) {
            mFirstTouchId = mSecondTouchId
        }

        if (mSecondTouchId == localId) {
            mSecondTouchId = -1
        }

        if (mFirstTouchId == -1) {
            return
        }

        val firstIndex = event.findPointerIndex(
            mFirstTouchId
        )

        mPivotX = event.getX(
            firstIndex
        )

        mPivotY = event.getY(
            firstIndex
        )

        mPrevX = mPivotX
        mPrevY = mPivotY

        mTranslateX = mTranslate2X
        mTranslateY = mTranslate2Y

        mFirstTouchId = -1
    }

    private inline fun actionMove(
        event: MotionEvent,
        touchIndex: Int
    ) = when {
        mSecondTouchId != -1 -> {

            val indexFirst = event.findPointerIndex(
                mFirstTouchId
            )

            val indexSecond = event.findPointerIndex(
                mSecondTouchId
            )

            val x = event.getX(
                mFirstTouchId
            )

            val y = event.getY(
                indexFirst
            )

            val xx = event.getX(
                indexSecond
            )
            val yy = event.getY(
                indexSecond
            )

            mCurrentDistance = hypot(
                xx - x,
                yy - y
            )

            mScaleDt = (mPrevDistance - mCurrentDistance) * SCALE_FACTOR
            scale += mScaleDt
            /*if (mScale > 7f) {
                mScale = 7f
            }
            if (mScale < 0.4f) {
                mScale = 0.4f
            }*/

            onScale?.onScale(
                scale
            )

            mPrevDistance = mCurrentDistance
        }

        else -> {
            val x = event.getX(touchIndex)
            val y = event.getY(touchIndex)
            mTranslate2X = mTranslateX + x - mPivotX
            mTranslate2Y = mTranslateY + y - mPivotY

            val dx = mPrevX - x
            val dy = y - mPrevY
            onDelta?.onDelta(
                dx, dy
            )

            mPrevX = x
            mPrevY = y
        }
    }
}