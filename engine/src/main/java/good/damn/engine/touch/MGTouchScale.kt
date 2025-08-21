package good.damn.engine.touch

import android.util.Log
import android.view.MotionEvent
import kotlin.math.hypot

class MGTouchScale
: MGTouchBound() {

    companion object {
        private const val SCALE_FACTOR = 0.1f
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

    private var mFirstTouchIndex = -1
    private var mSecondTouchIndex = -1

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

        if (mFirstTouchIndex == -1) {
            mPivotX = event.getX(
                touchIndex
            )

            mPivotY = event.getY(
                touchIndex
            )

            mPrevX = mPivotX
            mPrevY = mPivotY
            mFirstTouchIndex = touchIndex
            return true
        }

        event.run {
            mPrevDistance = hypot(
                getX(touchIndex) - getX(mFirstTouchIndex),
                getY(touchIndex) - getY(mFirstTouchIndex)
            )
        }

        mSecondTouchIndex = touchIndex
        return true
    }

    override fun onTouchMove(
        event: MotionEvent,
        touchIndex: Int
    ) = actionMove(
        event,
        touchIndex
    )

    override fun onTouchUp(
        event: MotionEvent,
        touchIndex: Int
    ) {
        if (mFirstTouchIndex == touchIndex) {
            mFirstTouchIndex = mSecondTouchIndex
        }

        if (mSecondTouchIndex == touchIndex) {
            mSecondTouchIndex = -1
        }

        if (mFirstTouchIndex == -1) {
            return
        }

        mPivotX = event.getX(
            mFirstTouchIndex
        )

        mPivotY = event.getY(
            mFirstTouchIndex
        )

        mPrevX = mPivotX
        mPrevY = mPivotY

        mTranslateX = mTranslate2X
        mTranslateY = mTranslate2Y
    }

    private inline fun actionMove(
        event: MotionEvent,
        touchIndex: Int
    ) = when {
        mSecondTouchIndex == -1 -> {
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

        else -> {
            val x = event.getX(mFirstTouchIndex)
            val y = event.getY(mFirstTouchIndex)

            val xx = event.getX(mSecondTouchIndex)
            val yy = event.getY(mSecondTouchIndex)

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
    }
}