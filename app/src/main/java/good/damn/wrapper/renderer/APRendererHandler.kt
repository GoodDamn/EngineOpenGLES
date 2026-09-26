package good.damn.wrapper.renderer

import android.opengl.GLSurfaceView
import android.util.DisplayMetrics
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.util.Log
import good.damn.common.COHandlerGlExecutor
import good.damn.engine2.utils.MGUtilsFile
import good.damn.wrapper.controllers.APControllerVr

class APRendererHandler(
    private val handlerExecutor: COHandlerGlExecutor,
    private val controllerVr: APControllerVr,
    private val displayMetrics: DisplayMetrics
): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        MGUtilsFile.glWriteExtensions()
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) {
        Log.d(TAG, "onSurfaceChanged: ${Thread.currentThread().name}")
        controllerVr.setScreenParams(
            width,
            height,
            displayMetrics.xdpi,
            displayMetrics.ydpi
        )
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        handlerExecutor.runTasksBounds(
            controllerVr.width,
            controllerVr.height
        )

        controllerVr.draw()
    }
}