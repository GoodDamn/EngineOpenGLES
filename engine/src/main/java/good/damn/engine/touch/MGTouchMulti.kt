package good.damn.engine.touch

import android.view.MotionEvent

open class MGTouchMulti
: MGITouchable {

    val touchIds = ArrayList<Int>(2)

    final override fun onTouchEvent(
        event: MotionEvent
    ) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (touchIds.isNotEmpty()) {
                    return
                }

                val index = event.actionIndex

                if (!onTouchDown(
                    event,
                    index
                )) {
                    return
                }

                touchIds.add(
                    event.getPointerId(
                        index
                    )
                )
            }

            MotionEvent.ACTION_MOVE -> {
                if (touchIds.isEmpty()) {
                    return
                }
                onTouchMove(
                    event,
                    event.findPointerIndex(
                        touchIds[0]
                    )
                )
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_UP -> {
                if (touchIds.isEmpty()) {
                    return
                }
                val index = event.actionIndex
                if (event.findPointerIndex(
                    touchIds[0]
                ) == index) {
                    touchIds.remove(
                        element = touchIds[0]
                    )
                    onTouchUp(
                        event,
                        index
                    )
                }
            }

            MotionEvent.ACTION_UP -> {
                touchIds.clear()
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