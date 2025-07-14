package good.damn.opengles_engine.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import good.damn.opengles_engine.activities.LevelEditorActivity
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
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

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mRenderer.onTouchDown(
                    event
                )
            }

            MotionEvent.ACTION_MOVE -> {
                mRenderer.onTouchMove(
                    event
                )
            }

            MotionEvent.ACTION_UP -> {
                mRenderer.onTouchUp(
                    event.x,
                    event.y
                )
            }
        }

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