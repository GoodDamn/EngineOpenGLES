package good.damn.wrapper.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.OnMeshPositionListener
import good.damn.engine.opengl.renderer.LevelEditorRenderer

class LevelEditorView(
    context: Context,
    requesterUserContent: MGIRequestUserContent
): GLSurfaceView(
    context
), OnMeshPositionListener {

    companion object {
        private const val TAG = "LevelEditorView"
    }

    private val mRenderer: LevelEditorRenderer

    init {
        setEGLContextClientVersion(
            3
        )

        mRenderer = LevelEditorRenderer(
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

    override fun onChangeMeshPosition(
        dx: Float,
        dy: Float,
        dz: Float
    ) {
        mRenderer.onChangeMeshPosition(
            dx, dy, dz
        )
    }
}