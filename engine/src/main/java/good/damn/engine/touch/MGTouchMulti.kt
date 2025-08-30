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
    ) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (mTouchIds.size >= maxTouches) {
                    return
                }

                val index = event.actionIndex

                if (!onTouchDown(
                    event,
                    index
                )) {
                    return
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
                mTouchIds.forEach {
                    val index = event.findPointerIndex(
                        it
                    )

                    if (index == -1) {
                        return
                    }

                    Log.d(javaClass.simpleName, "onTouchEvent: $index -> $it = $mTouchIds")
                    onTouchMove(
                        event,
                        index
                    )
                }
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_UP -> {
                if (mTouchIds.isEmpty()) {
                    return
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
        touchIndex: Int
    ) = Unit

    protected open fun onTouchUp(
        event: MotionEvent,
        touchIndex: Int
    ) = Unit
}