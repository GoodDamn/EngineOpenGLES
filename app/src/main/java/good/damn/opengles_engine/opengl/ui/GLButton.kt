package good.damn.opengles_engine.opengl.ui

import android.util.Log

class GLButton(
    private val mX: Float,
    private val mY: Float,
    private val mWidth: Float,
    private val mHeight: Float,
    private val mAction: () -> Unit
) {

    companion object {
        private const val TAG = "GLButton"
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