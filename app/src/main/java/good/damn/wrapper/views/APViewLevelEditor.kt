package good.damn.wrapper.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent
import good.damn.wrapper.renderer.APRendererHandler

class APViewLevelEditor(
    context: Context,
    private val mRenderer: APRendererHandler
): GLSurfaceView(
    context
) {

    companion object {
        private const val TAG = "APViewLevelEditor"
    }


    init {
        setEGLContextClientVersion(
            3
        )

        setEGLConfigChooser(
            8,8,8,8,
            24,
            8
        )

        //preserveEGLContextOnPause = true

        setRenderer(
            mRenderer
        )

        renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow: ")
        mRenderer.stop()
        super.onDetachedFromWindow()
    }

    override fun onTouchEvent(
        event: MotionEvent?
    ): Boolean {
        if (event == null) {
            return false
        }
        mRenderer.onTouchEvent(
            event
        )

        return true
    }
}