package good.damn.wrapper.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.util.Log
import good.damn.common.COHandlerGlExecutor
import good.damn.engine2.utils.MGUtilsFile

class APRendererHandler(
    private val handlerGlExecutor: COHandlerGlExecutor
): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private var mWidth = 0
    private var mHeight = 0

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

        mWidth = width
        mHeight = height
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        handlerGlExecutor.runTasksBounds(
            mWidth, mHeight
        )

        handlerGlExecutor.runCycle(
            mWidth, mHeight
        )
    }
}