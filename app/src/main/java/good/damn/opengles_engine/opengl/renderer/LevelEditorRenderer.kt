package good.damn.opengles_engine.opengl.renderer

import android.content.Context
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.util.Log
import good.damn.opengles_engine.activities.LevelEditorActivity
import good.damn.opengles_engine.opengl.Object3D
import good.damn.opengles_engine.opengl.StaticMesh
import good.damn.opengles_engine.opengl.camera.RotationCamera
import good.damn.opengles_engine.opengl.entities.Landscape
import good.damn.opengles_engine.opengl.light.DirectionalLight
import good.damn.opengles_engine.opengl.maps.DisplacementMap
import good.damn.opengles_engine.opengl.ui.GLButton
import good.damn.opengles_engine.utils.AssetUtils
import good.damn.opengles_engine.utils.ShaderUtils
import java.io.InputStream

class LevelEditorRenderer(
    var context: LevelEditorActivity
): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "LevelEditorRenderer"
    }

    private val mCamera = RotationCamera()

    private val mBtnZoomOut = GLButton {
        mCamera.radius += 2
    }

    private val mBtnZoomIn = GLButton {
        mCamera.radius -= 2
    }

    private val mBtnRandomizeLand = GLButton {
        mLandscape.randomizeY()
    }

    private val mBtnLoadDisplacementMap = GLButton {
        context.loadFromUserDisk(
            "*/*"
        )
    }

    private var mPrevX = 0f
    private var mPrevY = 0f

    private var mWidth = 0
    private var mHeight = 0

    private var mProgram = 0

    private lateinit var mDirectionalLight: DirectionalLight
    private lateinit var mLandscape: Landscape
    private lateinit var mSky: StaticMesh

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

        Log.d(TAG, "onSurfaceCreated: ")
        mProgram = glCreateProgram()

        glAttachShader(
            mProgram,
            ShaderUtils.createShader(
                GL_VERTEX_SHADER,
                AssetUtils.loadString(
                    "shaders/vert.glsl"
                )
            )
        )

        glAttachShader(
            mProgram,
            ShaderUtils.createShader(
                GL_FRAGMENT_SHADER,
                AssetUtils.loadString(
                    "shaders/frag.glsl"
                )
            )
        )

        glLinkProgram(
            mProgram
        )

        glUseProgram(
            mProgram
        )

        mDirectionalLight = DirectionalLight(
            mProgram
        )

        mLandscape = Landscape(
            mProgram,
            mCamera
        )

        mLandscape.setResolution(
            200,
            200
        )

        mLandscape.displace(
            DisplacementMap.createFromAssets(
                "maps/displace.png"
            )
        )

        mLandscape.setScale(
            5.0f,
            5.0f,
            5.0f
        )

        mSky = StaticMesh(
            Object3D.createFromAssets(
                "objs/sphere.obj"
            ),
            "textures/sky/skysphere_light.jpg",
            mProgram,
            mCamera
        )

        mCamera.radius = 350f

        mCamera.setRotation(
            0f,
            0.01f
        )

        mDirectionalLight.setPosition(
            10f,
            -100f,
            0f
        )

        mSky.setScale(
            100000f,
            100000f,
            100000f
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
        Log.d(TAG, "onSurfaceChanged: ")
        mWidth = width
        mHeight = height

        mCamera.setPerspective(
            width,
            height
        )

        val btnLen = mWidth * 0.1f

        mBtnZoomOut.bounds(
            0f,
            0f,
            btnLen,
            btnLen
        )

        mBtnZoomIn.bounds(
            btnLen,
            0f,
            btnLen,
            btnLen
        )

        mBtnRandomizeLand.bounds(
            mWidth - btnLen,
            0f,
            btnLen,
            btnLen
        )

        mBtnLoadDisplacementMap.bounds(
            mWidth - btnLen * 2,
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

        mLandscape.draw()
        mSky.draw()
        mDirectionalLight.draw()
    }

    fun onTouchDown(
        x: Float,
        y: Float
    ) {
        mPrevX = x
        mPrevY = y
        if (mBtnRandomizeLand.intercept(x,y) ||
            mBtnLoadDisplacementMap.intercept(x,y)
        ) {
            return
        }
    }

    fun onTouchMove(
        x: Float,
        y: Float
    ) {
        if (mBtnZoomOut.intercept(x, y) ||
            mBtnZoomIn.intercept(x,y)
        ) {
            return
        }

        mCamera.rotateBy(
            (mPrevX - x) * 0.001f,
            (y - mPrevY) * 0.001f
        )

        mPrevX = x
        mPrevY = y
    }

    fun onTouchUp(
        x: Float,
        y: Float
    ) {

    }

    fun onLoadFromUserDisk(
        inp: InputStream
    ) {
        mLandscape.displace(
            DisplacementMap.createFromStream(
                inp
            )
        )
    }

}