package good.damn.engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.util.Log
import android.view.MotionEvent
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.EditorMesh
import good.damn.engine.opengl.camera.RotationCamera
import good.damn.engine.opengl.entities.Landscape
import good.damn.engine.opengl.entities.SkySphere
import good.damn.engine.opengl.light.DirectionalLight
import good.damn.engine.opengl.maps.DisplacementMap
import good.damn.engine.opengl.models.UserContent
import good.damn.engine.opengl.thread.GLHandler
import good.damn.engine.opengl.ui.GLButton
import good.damn.engine.touch.MGIListenerTransform
import good.damn.engine.touch.MGTouchScale
import good.damn.engine.utils.MGUtilsShader
import java.util.LinkedList

class LevelEditorRenderer(
    private val requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer,
MGIListenerOnGetUserContent,
MGIListenerTransform {

    companion object {
        private const val TAG = "LevelEditorRenderer"
    }

    private val mHandler = GLHandler()

    private val meshes = LinkedList<EditorMesh>()

    private val mCamera = RotationCamera()

    private val mTouchScale = MGTouchScale().apply {
        listener = this@LevelEditorRenderer
    }

    private val mBtnLoadUserContent = GLButton {
        requesterUserContent.requestUserContent(
            this,
            "*/*"
        )
    }

    private var mWidth = 0
    private var mHeight = 0

    private var mProgram = 0

    private lateinit var mDirectionalLight: DirectionalLight
    private lateinit var mLandscape: Landscape
    private lateinit var mSky: SkySphere

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        mProgram = MGUtilsShader.createProgramFromAssets(
            "shaders/vert.glsl",
            "shaders/frag.glsl"
        )

        glLinkProgram(
            mProgram
        )

        mCamera.radius = 350f

        mCamera.setRotation(
            0f,
            0.01f
        )

        mDirectionalLight = DirectionalLight(
            mProgram
        )

        mDirectionalLight.setPosition(
            10f,
            -100f,
            0f
        )

        mLandscape = Landscape(
            mProgram
        )

        mLandscape.setResolution(
            256,
            256
        )

        mLandscape.displace(
            DisplacementMap.createFromAssets(
                "maps/displace.png"
            )
        )

        mLandscape.setScale(
            10.0f,
            10.0f,
            10.0f
        )

        mSky = SkySphere(
            mProgram
        )

        glUseProgram(
            mProgram
        )

        glEnable(
            GL_DEPTH_TEST
        )
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) {
        Log.d(TAG, "onSurfaceChanged: ${Thread.currentThread().name}")
        mWidth = width
        mHeight = height

        mCamera.setPerspective(
            width,
            height
        )

        val btnLen = mWidth * 0.1f

        mBtnLoadUserContent.bounds(
            mWidth - btnLen,
            0f,
            btnLen,
            btnLen
        )
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        glViewport(
            0,
            0,
            mWidth,
            mHeight
        )

        glClear(GL_COLOR_BUFFER_BIT or
            GL_DEPTH_BUFFER_BIT
        )

        glClearColor(
            0.2f,
                0.2f,
            0.2f,
            1.0f
        )

        mHandler.run()

        mLandscape.draw(
            mCamera
        )
        meshes.forEach { editorMesh ->
            editorMesh.mesh!!.draw(
                mCamera
            )
        }
        mSky.draw(
            mCamera
        )
        mDirectionalLight.draw()
    }

    override fun onGetUserContent(
        userContent: UserContent
    ) {
        mLandscape.displace(
            DisplacementMap.createFromStream(
                userContent.stream
            )
        )
    }

    fun onTouchEvent(
        event: MotionEvent
    ) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.pointerCount != 1) {
                    return
                }

                if (mBtnLoadUserContent.intercept(event.x, event.y)) {
                    return
                }
            }
        }

        mTouchScale.onTouchEvent(
            event
        )
    }

    override fun onRotate(
        dx: Float,
        dy: Float
    ) {
        mCamera.rotateBy(
            dx * 0.001f,
            dy * 0.001f
        )
    }

    override fun onScale(
        scale: Float
    ) {
        mCamera.radius = scale
    }
}