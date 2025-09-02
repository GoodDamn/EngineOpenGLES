package good.damn.engine.touch

import android.util.Log
import android.view.MotionEvent

open class MGTouchMulti(
    private val maxTouches: Int
): MGITouchable {

    private val mTouchIds = ArrayList<Int>(
        maxTouches
    )

    final override fun onTouchEvent(
        event: MotionEvent
    ): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (mTouchIds.size >= maxTouches) {
                    return false
                }

                val index = event.actionIndex

                if (!onTouchDown(
                    event,
                    index
                )) {
                    return false
                }

                val ptrId = event.getPointerId(
                    index
                )
                Log.d(javaClass.simpleName, "onTouchEvent: ACTION_DOWN: $index -> $ptrId")
                mTouchIds.add(
                    ptrId
                )
            }

            MotionEvent.ACTION_MOVE -> {
                if (mTouchIds.isEmpty()) {
                    return false
                }
                onTouchMove(
                    event,
                    mTouchIds
                )
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_UP -> {
                if (mTouchIds.isEmpty()) {
                    return false
                }

                val index = event.actionIndex
                val ptrId = event.getPointerId(
                    index
                )

                Log.d(javaClass.simpleName, "onTouchEvent: ACTION_UP: $ptrId=$mTouchIds")
                if (mTouchIds.remove(
                    element = ptrId
                )) {
                    Log.d(javaClass.simpleName, "onTouchEvent: $ptrId removed")
                    onTouchUp(
                        event,
                        index
                    )
                }
            }

            MotionEvent.ACTION_UP -> {
                mTouchIds.clear()
                onTouchUp(
                    event,
                    0
                )
            }
        }

        return true
    }

    fun containsMotionEvent(
        event: MotionEvent
    ) = mTouchIds.contains(
        event.getPointerId(
            event.actionIndex
        )
    )

    protected open fun onTouchDown(
        event: MotionEvent,
        touchIndex: Int
    ) = true

    protected open fun onTouchMove(
        event: MotionEvent,
        touchIds: List<Int>
    ) = Unit

    protected open fun onTouchUp(
        event: MotionEvent,
        touchIndex: Int
    ) = Unit
}