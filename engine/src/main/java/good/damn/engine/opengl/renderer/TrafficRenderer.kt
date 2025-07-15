package good.damn.engine.opengl.renderer

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import good.damn.engine.opengl.Object3D
import good.damn.engine.opengl.StaticMesh
import good.damn.engine.opengl.camera.RotationCamera
import good.damn.engine.opengl.entities.Mesh
import good.damn.engine.opengl.light.DirectionalLight
import good.damn.engine.utils.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TrafficRenderer
: GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "TrafficRenderer"
    }

    private var mSumTick = 0f
    private var mCurrentMillis = 0L
    private var mPrevMillis = 0L
    private var mTick = 0f

    private var mWidth = 0
    private var mHeight = 0

    private var mProgram = 0

    private lateinit var mEntities: Array<Mesh>
    private lateinit var mDirectionalLight: DirectionalLight
    private var mCamera = RotationCamera()

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

        mProgram = ShaderUtils.createProgramFromAssets(
            "shaders/vert.glsl",
            "shaders/frag.glsl"
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

        mEntities = arrayOf(
            StaticMesh(
                Object3D.createFromAssets(
                    "objs/plane.obj"
                ),
                "textures/grass.jpg",
                mProgram
            ),
            StaticMesh(
                Object3D.createFromAssets(
                    "objs/box.obj"
                ),
                "textures/rock.jpg",
                mProgram
            ),
            StaticMesh(
                Object3D.createFromAssets(
                    "objs/sphere.obj"
                ),
                "textures/rock.jpg",
                mProgram
            ),
            StaticMesh(
                Object3D.createFromAssets(
                    "objs/walls.obj"
                ),
                "textures/rock.jpg",
                mProgram
            )
        )

        mEntities[1].setPosition(
            0f,
            0.5f,
            0f
        )

        mEntities[2].setPosition(
            1f,
            0.5f,
            0f
        )

        glEnable(
            GL_DEPTH_TEST
        )

        mPrevMillis = System.currentTimeMillis()
    }

    override fun onSurfaceChanged(
        p0: GL10?,
        width: Int,
        height: Int
    ) {
        mWidth = width
        mHeight = height
        mCamera.radius = 6f

        mCamera.setPerspective(
            width,
            height
        )
    }

    override fun onDrawFrame(p0: GL10?) {
        mCurrentMillis = System.currentTimeMillis()

        glClear(
            GL_COLOR_BUFFER_BIT or
            GL_DEPTH_BUFFER_BIT
        )

        glClearColor(
            0f,
            0.1f,
            0.1f,
            1f
        )

        glViewport(
            0,
            0,
            mWidth,
            mHeight
        )

        mDirectionalLight.draw()

        mEntities.forEach {
            it.draw(
                mCamera
            )
        }

        mTick = (mCurrentMillis - mPrevMillis) / 1000.0f

        mCamera.rotateBy(
            mTick * 0.25f,
            0f
        )

        mSumTick += mTick
        mPrevMillis = mCurrentMillis
    }
}