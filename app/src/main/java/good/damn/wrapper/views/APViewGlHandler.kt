package good.damn.wrapper.views

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent
import good.damn.wrapper.hud.APHud
import good.damn.wrapper.renderer.APRendererEditor
import good.damn.wrapper.renderer.APRendererHandler

@SuppressLint("ViewConstructor")
class APViewGlHandler(
    context: Context,
    renderer: APRendererHandler,
    private val mHud: APHud
): GLSurfaceView(
    context
) {

    companion object {
        private const val TAG = "APViewGlHandler"
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
            renderer
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

        mHud.layerEditor.onTouchEvent(
            event
        )

        return true
    }
}