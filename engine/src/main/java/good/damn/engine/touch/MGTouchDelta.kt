package good.damn.engine.touch

import android.view.MotionEvent

class MGTouchDelta
: MGITouchable {

    private var mHasTouch = false

    private var mPrevX = 0f
    private var mPrevY = 0f

    override fun onTouchEvent(
        event: MotionEvent
    ) {
        when (event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                mPrevX = event.x
                mPrevY = event.y

                mHasTouch = true
            }

            MotionEvent.ACTION_MOVE -> {
                if (!mHasTouch) {
                    return
                }

                val dx = mPrevX - event.x
                val dy = event.y - mPrevY

                mPrevX = event.x
                mPrevY = event.y
            }

            MotionEvent.ACTION_POINTER_UP -> {
                mHasTouch = false
            }
        }
    }

    private fun processDown(
        event: MotionEvent
    ) {

    }
}