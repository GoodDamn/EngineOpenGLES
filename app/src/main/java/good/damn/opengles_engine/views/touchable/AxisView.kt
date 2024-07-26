package good.damn.opengles_engine.views.touchable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View

class AxisView(
    context: Context
): View(
    context
), View.OnTouchListener {

    private val mPaint = Paint()

    private var mDeltaY = 0
    private var mDeltaX = 0f

    init {
        setOnTouchListener(this)
    }

    var axisMoves: Array<Axis>? = null

    private val mRectAxis = Rect()

    private var mSelectedAxis: Axis? = null

    override fun onDraw(
        canvas: Canvas
    ) {
        super.onDraw(
            canvas
        )

        mRectAxis.top = 0
        mRectAxis.bottom = 0

        axisMoves?.forEach {
            mPaint.color = it.color
            mRectAxis.bottom += mDeltaY
            canvas.drawRect(
                mRectAxis,
                mPaint
            )

            mRectAxis.top += mDeltaY
        }
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(
            changed,
            left,
            top,
            right,
            bottom
        )

        axisMoves?.let {
            mDeltaY = height / it.size
        }

        mRectAxis.left = 0
        mRectAxis.right = width
    }

    override fun onTouch(
        v: View?,
        event: MotionEvent?
    ): Boolean {
        if (event == null) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                axisMoves?.let {
                    var c = mDeltaY

                    for (i in it) {
                        if (event.y < c) {
                            mSelectedAxis = i
                            break
                        }

                        c += mDeltaY
                    }

                    mDeltaX = event.x
                    return true
                }

                return false
            }

            MotionEvent.ACTION_MOVE -> {
                mSelectedAxis?.onMove?.invoke(
                    mDeltaX - event.x
                )
                mDeltaX = event.x
            }

        }
        return true
    }

}