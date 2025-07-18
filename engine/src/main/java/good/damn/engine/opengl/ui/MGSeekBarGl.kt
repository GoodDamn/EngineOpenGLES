package good.damn.engine.opengl.ui

import android.util.Log

class MGSeekBarGl(
    private var mX: Float = 0f,
    private var mY: Float = 0f,
    width: Float = 0f,
    height: Float = 0f,
    private val onChangeValue: (Float) -> Unit
) {

    companion object {
        private const val TAG = "MGSeekBarGl"
    }

    private var mRight = mX + width
    private var mBottom = mY + height

    fun bounds(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) {
        mX = left
        mY = top
        mRight = right
        mBottom = bottom
    }

    fun intercept(
        x: Float,
        y: Float
    ): Boolean {
        Log.d(TAG, "intercept: $mX $x $mRight;;;;;$mY $y $mBottom")
        if (mX > x || x > mRight) {
            return false
        }

        if (mY > y || y > mBottom) {
            return false
        }

        val dth = (y - mY) / (mBottom - mY)

        onChangeValue(
            dth
        )

        return true
    }
}