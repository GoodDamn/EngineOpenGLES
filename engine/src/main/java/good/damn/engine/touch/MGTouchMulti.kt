package good.damn.engine.touch

import android.util.Log
import android.view.MotionEvent

open class MGTouchMulti
: MGITouchable {

    private var mTouchId = -1

    final override fun onTouchEvent(
        event: MotionEvent
    ) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                if (mTouchId != -1) {
                    return
                }

                if (onTouchDown(
                    event,
                    index
                )) {
                    return
                }

                mTouchId = event.getPointerId(
                    index
                )
            }

            MotionEvent.ACTION_MOVE -> {
                if (mTouchId == -1) {
                    return
                }
                onTouchMove(
                    event,
                    event.findPointerIndex(
                        mTouchId
                    )
                )
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_UP -> {
                val index = event.actionIndex
                if (event.findPointerIndex(mTouchId) == index) {
                    mTouchId = -1
                }
                onTouchUp(
                    event,
                    index
                )
            }

            MotionEvent.ACTION_UP -> {
                mTouchId = -1
                onTouchUp(
                    event,
                    0
                )
            }
        }
    }

    protected open fun onTouchDown(
        event: MotionEvent,
        touchIndex: Int
    ) = true

    protected open fun onTouchMove(
        event: MotionEvent,
        touchIndex: Int
    ) = Unit

    protected open fun onTouchUp(
        event: MotionEvent,
        touchIndex: Int
    ) = Unit
}