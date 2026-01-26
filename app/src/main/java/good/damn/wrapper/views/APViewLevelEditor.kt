package good.damn.wrapper.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent
import good.damn.wrapper.interfaces.APIRequestUserContent
import good.damn.wrapper.renderer.APRendererLevelEditor

class APViewLevelEditor(
    context: Context,
    requesterUserContent: APIRequestUserContent
): GLSurfaceView(
    context
) {

    companion object {
        private const val TAG = "APViewLevelEditor"
    }

    private val mRenderer: APRendererLevelEditor

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

        mRenderer = APRendererLevelEditor(
            requesterUserContent
        )

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