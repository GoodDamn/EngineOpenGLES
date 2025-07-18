package good.damn.engine.opengl.ui

import android.util.Log

class MGButtonGL(
    private var mX: Float = 0f,
    private var mY: Float = 0f,
    private var mWidth: Float = 0f,
    private var mHeight: Float = 0f,
    private val mAction: () -> Unit
) {
    companion object {
        private const val TAG = "GLButton"
    }

    fun bounds(
        x: Float,
        y: Float,
        width: Float,
        height: Float
    ) {
        mX = x
        mY = y
        mWidth = width
        mHeight = height
    }

    fun intercept(
        x: Float,
        y: Float
    ): Boolean {
        Log.d(TAG, "intercept: $mX $x ${mX+mWidth};;;;;$mY $y ${mY+mHeight}")
        if (mX > x || x > mX + mWidth) {
            return false
        }

        if (mY > y || y > mY + mHeight) {
            return false
        }

        mAction()
        return true
    }

}