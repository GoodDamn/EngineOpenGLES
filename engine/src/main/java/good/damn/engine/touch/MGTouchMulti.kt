package good.damn.engine.touch

import android.view.MotionEvent

open class MGTouchMulti(
    private val maxTouches: Int = 1
): MGITouchable {

    val touchIds = ArrayList<Int>(
        maxTouches
    )

    final override fun onTouchEvent(
        event: MotionEvent
    ) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (touchIds.size >= maxTouches) {
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
                val ptrId = event.getPointerId(
                    event.actionIndex
                )

                if (!touchIds.contains(ptrId)) {
                    return
                }

                onTouchMove(
                    event,
                    event.findPointerIndex(
                        ptrId
                    )
                )
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_UP -> {
                if (touchIds.isEmpty()) {
                    return
                }

                val index = event.actionIndex
                val ptrId = event.getPointerId(
                    index
                )

                if (!touchIds.contains(
                    ptrId
                )) {
                    return
                }

                if (event.findPointerIndex(
                    ptrId
                ) == index) {
                    touchIds.remove(
                        element = ptrId
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