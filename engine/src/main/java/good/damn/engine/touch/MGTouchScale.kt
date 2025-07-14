package good.damn.engine.touch

import android.view.MotionEvent
import kotlin.math.hypot

class MGTouchScale {

    companion object {
        private const val SCALE_FACTOR = 0.25f
    }

    var listener: MGIListenerTransform? = null

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

    private var mScale = 1.0f
    private var mScaleDt = 0f

    fun onTouchEvent(
        event: MotionEvent
    ): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPivotX = event.x
                mPivotY = event.y

                mPrevX = mPivotX
                mPrevY = mPivotY
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    event.apply {
                        mPrevDistance = hypot(
                            getX(1) - getX(0),
                            getY(1) - getY(0)
                        )

                    }
                }
            }

            MotionEvent.ACTION_MOVE -> actionMove(
                event
            )

            MotionEvent.ACTION_POINTER_UP -> {
                if (event.pointerCount == 2) {
                    val pivotIndex = if (
                        event.actionIndex == 0
                    ) 1 else 0

                    mPivotX = event.getX(pivotIndex)
                    mPivotY = event.getY(pivotIndex)
                    mTranslateX = mTranslate2X
                    mTranslateY = mTranslate2Y
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                mTranslateX = mTranslate2X
                mTranslateY = mTranslate2Y
            }
        }

        return true
    }

    private inline fun actionMove(
        event: MotionEvent
    ) = when {
        event.pointerCount == 1 -> {
            mTranslate2X = mTranslateX + event.x - mPivotX
            mTranslate2Y = mTranslateY + event.y - mPivotY
//            listener?.onTranslate(
//                mTranslate2X,
//                mTranslate2Y
//            )

            val dx = mPrevX - event.x
            val dy = event.y - mPrevY
            listener?.onRotate(
                dx, dy
            )

            mPrevX = event.x
            mPrevY = event.y
        }

        event.pointerCount > 1 -> {
            val x = event.getX(0)
            val y = event.getY(0)

            val xx = event.getX(1)
            val yy = event.getY(1)

            mCurrentDistance = hypot(
                xx - x,
                yy - y
            )

            mScaleDt = (mPrevDistance - mCurrentDistance) * SCALE_FACTOR
            mScale += mScaleDt
            /*if (mScale > 7f) {
                mScale = 7f
            }
            if (mScale < 0.4f) {
                mScale = 0.4f
            }*/

            listener?.onScale(
                mScale
            )

            mPrevDistance = mCurrentDistance
        }
        else -> Unit
    }
}