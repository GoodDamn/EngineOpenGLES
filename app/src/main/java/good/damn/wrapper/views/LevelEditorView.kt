package good.damn.wrapper.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.renderer.MGRendererLevelEditor

class LevelEditorView(
    context: Context,
    requesterUserContent: MGIRequestUserContent
): GLSurfaceView(
    context
) {

    companion object {
        private const val TAG = "LevelEditorView"
    }

    private val mRenderer: MGRendererLevelEditor

    init {
        setEGLContextClientVersion(
            3
        )

        mRenderer = MGRendererLevelEditor(
            requesterUserContent
        )

        setRenderer(
            mRenderer
        )

        renderMode = RENDERMODE_CONTINUOUSLY
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