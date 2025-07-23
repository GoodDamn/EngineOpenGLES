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
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.entities.MGLandscape
import good.damn.engine.opengl.entities.MGSkySphere
import good.damn.engine.opengl.light.MGLightDirectional
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.ui.MGButtonGL
import good.damn.engine.opengl.ui.MGSeekBarGl
import good.damn.engine.touch.MGIListenerMove
import good.damn.engine.touch.MGIListenerDelta
import good.damn.engine.touch.MGIListenerScale
import good.damn.engine.touch.MGTouchFreeMove
import good.damn.engine.touch.MGTouchMove
import good.damn.engine.touch.MGTouchScale
import good.damn.engine.utils.MGUtilsShader
import kotlin.math.cos
import kotlin.math.sin

class MGRendererLevelEditor(
    private val requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer,
MGIListenerOnGetUserContent,
MGIListenerScale,
MGIListenerDelta,
MGIListenerMove {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mCameraFree = MGCameraFree()

    private val mTouchScale = MGTouchScale().apply {
        onScale = this@MGRendererLevelEditor
        onDelta = this@MGRendererLevelEditor
    }

    private val mTouchMove = MGTouchFreeMove().apply {
        setListenerMove(
            this@MGRendererLevelEditor
        )
        setListenerDelta(
            this@MGRendererLevelEditor
        )
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

        /*mCameraRotation.radius = 1250f
        mTouchScale.scale = mCameraRotation.radius

        mCameraRotation.setRotation(
            0f,
            0.01f
        )*/

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

        val fWidth = width.toFloat()
        val fHeight = height.toFloat()

        mCameraFree.setPerspective(
            width,
            height
        )

        val w: Float
        val h: Float

        if (width < height) {
            w = fWidth
            h = fHeight
        } else {
            w = fHeight
            h = fWidth
        }

        val s = h * 0.25f
        mTouchMove.setBoundsMove(
            0f,
            fHeight - s - fHeight * 0.1f,
            s
        )

        val btnLen = w * 0.1f

        mTouchMove.setBoundsDelta(
            fWidth * 0.5f,
            0f,
            fWidth,
            fHeight
        )

        mBarSeekAmbient.bounds(
            0f,
            0f,
            btnLen,
            fHeight
        )

        mBtnSwitchWireframe.bounds(
            fWidth - btnLen,
            fHeight - btnLen,
            btnLen,
            btnLen
        )

        mBtnLoadUserContent.bounds(
            fWidth - btnLen,
            0f,
            btnLen,
            btnLen
        )
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        val f = System.currentTimeMillis() % 100000L * 0.001f
        val fx = sin(f) * 840f
        val fz = cos(f) * 840f

        mDirectionalLight.setPosition(
            fx, 600f, fz
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
            mCameraFree
        )
        mSky.draw(
            mCameraFree
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
        if (event.pointerCount == 1 && event.action == MotionEvent.ACTION_UP) {
            /*if (mBarSeekAmbient.intercept(event.x, event.y)) {
                return
            }*/

            if (mBtnLoadUserContent.intercept(event.x, event.y)) {
                return
            }

            if (mBtnSwitchWireframe.intercept(event.x, event.y)) {
                return
            }
        }

        mTouchMove.onTouchEvent(
            event
        )
    }

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        mCameraFree.addRotation(
            dx * 0.001f,
            dy * 0.001f
        )
        mCameraFree.invalidatePosition()
        /*mCameraRotation.rotateBy(
            dx * 0.001f,
            dy * 0.001f
        )*/
    }

    override fun onScale(
        scale: Float
    ) {
        //mCameraRotation.radius = scale
    }

    override fun onMove(
        x: Float,
        y: Float
    ) {
        mCameraFree.addPosition(
            x, y
        )
        mCameraFree.invalidatePosition()
    }
}