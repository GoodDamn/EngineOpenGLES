package good.damn.wrapper.views

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent
import good.damn.logic.process.LGManagerProcessTime
import good.damn.wrapper.hud.APHud
import good.damn.wrapper.renderer.APRendererHandler

@SuppressLint("ViewConstructor")
class APViewGlHandler(
    context: Context,
    private val managerProcess: LGManagerProcessTime,
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
        managerProcess.apply {
            stop()
            unregisterAll()
        }
        super.onDetachedFromWindow()
    }

    override fun onLayout(
        changed: Boolean,
        left: Int, top: Int,
        right: Int, bottom: Int
    ) {
        super.onLayout(
            changed,
            left, top,
            right, bottom
        )


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