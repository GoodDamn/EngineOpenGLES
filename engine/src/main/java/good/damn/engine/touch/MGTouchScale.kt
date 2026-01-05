package good.damn.engine.touch

import android.view.MotionEvent
import java.util.LinkedList
import kotlin.math.hypot

class MGTouchScale
: MGTouchBound(
    maxTouches = 3
) {

    companion object {
        private const val SCALE_FACTOR = -0.0001f
    }

    var onDistance: MGIListenerDistance? = null
    var onScale: MGIListenerScale? = null
    var onDelta: MGIListenerDelta? = null

    private var mPrevX = 0f
    private var mPrevY = 0f

    private var mPrevDistance = 0f
    private var mCurrentDistance = 0f

    private var mScaleDt = 0f

    private var mPrevY3 = 0f

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

        if (touchIds.size == 1) {
            mPrevX = event.getX(
                touchIndex
            )

            mPrevY = event.getY(
                touchIndex
            )
            return true
        }

        if (touchIds.size == 2) {
            calculateTwoTouches(
                event,
                touchIds
            )
            return true
        }

        mPrevY3 = calculateMiddleY3(
            event,
            touchIds
        )
        return true
    }

    override fun onTouchMove(
        event: MotionEvent,
        touchIds: List<Int>
    ) {
        actionMove(
            event,
            touchIds
        )
    }

    override fun onTouchUp(
        event: MotionEvent,
        touchIndex: Int,
        touchIds: List<Int>
    ) {
        if (touchIds.size >= 2) {
            calculateTwoTouches(
                event,
                touchIds
            )
            return
        }

        if (touchIds.isNotEmpty()) {
            calculateFirstTouchId(
                event,
                touchIds
            )
        }
    }

    private inline fun actionMove(
        event: MotionEvent,
        touchIds: List<Int>
    ) = when {
        touchIds.size >= 3 -> {
            val now = calculateMiddleY3(
                event,
                touchIds
            )

            val dt = mPrevY3 - now
            mPrevY3 = now

            onDistance?.onDistance(
                dt
            )
        }

        touchIds.size == 2 -> {
            val indexFirst = event.findPointerIndex(
                touchIds[0]
            )

            val indexSecond = event.findPointerIndex(
                touchIds[1]
            )

            mCurrentDistance = event.run {
                hypot(
                    getX(indexSecond) - getX(indexFirst),
                    getY(indexSecond)- getY(indexFirst)
                )
            }

            mScaleDt = mPrevDistance - mCurrentDistance

            onScale?.onScale(
                mScaleDt * SCALE_FACTOR
            )

            mPrevDistance = mCurrentDistance
        }

        else -> {
            val touchIndex = event.findPointerIndex(
                touchIds[0]
            )

            val x = event.getX(
                touchIndex
            )

            val y = event.getY(
                touchIndex
            )

            val dx = mPrevX - x
            val dy = y - mPrevY
            onDelta?.onDelta(
                dx, dy
            )

            mPrevX = x
            mPrevY = y
        }
    }

    private fun calculateFirstTouchId(
        event: MotionEvent,
        touchIds: List<Int>
    ) {
        val firstIndex = event.findPointerIndex(
            touchIds[0]
        )

        mPrevX = event.getX(
            firstIndex
        )

        mPrevY = event.getY(
            firstIndex
        )
    }

    private fun calculateTwoTouches(
        event: MotionEvent,
        touchIds: List<Int>
    ) {
        val touchIndexFirst = event.findPointerIndex(
            touchIds[0]
        )

        val touchIndexSecond = event.findPointerIndex(
            touchIds[1]
        )

        mPrevDistance = event.run {
            hypot(
                getX(touchIndexSecond) - getX(touchIndexFirst),
                getY(touchIndexSecond) - getY(touchIndexFirst)
            )
        }
    }

    private fun calculateMiddleY3(
        event: MotionEvent,
        touchIds: List<Int>
    ): Float {
        val indexFirst = event.findPointerIndex(
            touchIds[0]
        )

        val indexSecond = event.findPointerIndex(
            touchIds[1]
        )

        val indexThird = event.findPointerIndex(
            touchIds[2]
        )

        val y1 = event.getY(
            indexFirst
        )

        val y2 = event.getY(
            indexSecond
        )

        val y3 = event.getY(
            indexThird
        )

        val dty = (
            y3 + y2 + y1
        ) / 3

        return dty
    }
}