package good.damn.engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import good.damn.engine.MGEngine
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.camera.MGCameraRotation
import good.damn.engine.opengl.entities.MGLandscape
import good.damn.engine.opengl.entities.MGSkySphere
import good.damn.engine.opengl.light.MGLightDirectional
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.ui.MGButtonGL
import good.damn.engine.opengl.ui.MGSeekBarGl
import good.damn.engine.touch.MGIListenerTransform
import good.damn.engine.touch.MGTouchScale
import good.damn.engine.utils.MGUtilsShader
import kotlin.math.cos
import kotlin.math.sin

class MGRendererLevelEditor(
    private val requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer,
MGIListenerOnGetUserContent,
MGIListenerTransform {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mCamera = MGCameraRotation()

    private val mTouchScale = MGTouchScale().apply {
        listener = this@MGRendererLevelEditor
    }

    private val mBtnLoadUserContent = MGButtonGL {
        requesterUserContent.requestUserContent(
            this,
            "*/*"
        )
    }

    private val mBtnSwitchWireframe = MGButtonGL {
        MGEngine.isWireframe = !MGEngine.isWireframe
        /*mHandler.post {
            glUseProgram(
                if (MGEngine.isWireframe) mProgramWireframe
                else mProgramDefault
            )
        }*/
    }

    private val mBarSeekAmbient = MGSeekBarGl {
        mDirectionalLight.ambient = it
    }

    private val mHandler = MGHandlerGl()

    private var mWidth = 0
    private var mHeight = 0

    private var mProgramWireframe = 0
    private var mProgramDefault = 0

    private lateinit var mDirectionalLight: MGLightDirectional
    private lateinit var mLandscape: MGLandscape
    private lateinit var mSky: MGSkySphere

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

        mProgramDefault = MGUtilsShader.createProgramFromAssets(
            "shaders/vert.glsl",
            "shaders/frag.glsl"
        )

        mProgramWireframe = MGUtilsShader.createProgramFromAssets(
            "shaders/vert.glsl",
            "shaders/frag_wireframe.glsl"
        )

        glLinkProgram(
            mProgramWireframe
        )

        glLinkProgram(
            mProgramDefault
        )

        glUseProgram(
            mProgramDefault
        )

        mCamera.radius = 1250f
        mTouchScale.scale = mCamera.radius

        mCamera.setRotation(
            0f,
            0.01f
        )

        mDirectionalLight = MGLightDirectional(
            mProgramDefault
        )

        mLandscape = MGLandscape(
            mProgramDefault
        ).apply {
            setResolution(
                mProgramDefault,
                1024,
                1024
            )

            displace(
                MGMapDisplace.createFromAssets(
                    "maps/terrain_height.png"
                )
            )
        }

        mLandscape.setScale(
            3.0f,
            3.0f,
            3.0f
        )

        mSky = MGSkySphere(
            mProgramDefault
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

        mBarSeekAmbient.bounds(
            0f,
            0f,
            btnLen,
            height.toFloat()
        )

        mBtnSwitchWireframe.bounds(
            width - btnLen,
            height - btnLen,
            btnLen,
            btnLen
        )

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
        val f = System.currentTimeMillis() % 100000L * 0.001f
        val fx = sin(f) * 440f
        val fz = cos(f) * 440f

        mDirectionalLight.setPosition(
            fx, 400f, fz
        )

        mHandler.run()
        //Log.d(TAG, "onDrawFrame: $mF")
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
            0.0f,
                0.0f,
            0.0f,
            1.0f
        )

        mDirectionalLight.draw()
        mLandscape.draw(
            mCamera
        )
        mSky.draw(
            mCamera
        )
    }

    override fun onGetUserContent(
        userContent: MGMUserContent
    ) {
        val mapDisplace = MGMapDisplace.createFromStream(
            userContent.stream
        )

        Handler(
            Looper.getMainLooper()
        ).post {
            mHandler.post {
                mLandscape.displace(
                    mapDisplace
                )
            }
        }
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

                if (mBtnSwitchWireframe.intercept(event.x, event.y)) {
                    return
                }
            }

            MotionEvent.ACTION_UP -> {
                if (mBarSeekAmbient.intercept(event.x, event.y)) {
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