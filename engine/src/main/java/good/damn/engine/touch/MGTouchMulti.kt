package good.damn.engine.touch

import android.view.MotionEvent

open class MGTouchMulti
: MGITouchable {

    var touchId = -1
        private set

    final override fun onTouchEvent(
        event: MotionEvent
    ) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                if (touchId != -1) {
                    return
                }

                if (!onTouchDown(
                    event,
                    index
                )) {
                    return
                }

                touchId = event.getPointerId(
                    index
                )
            }

            MotionEvent.ACTION_MOVE -> {
                if (touchId == -1) {
                    return
                }
                onTouchMove(
                    event,
                    event.findPointerIndex(
                        touchId
                    )
                )
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_UP -> {
                if (touchId == -1) {
                    return
                }
                val index = event.actionIndex
                if (event.findPointerIndex(touchId) == index) {
                    touchId = -1
                    onTouchUp(
                        event,
                        index
                    )
                }
            }

            MotionEvent.ACTION_UP -> {
                touchId = -1
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