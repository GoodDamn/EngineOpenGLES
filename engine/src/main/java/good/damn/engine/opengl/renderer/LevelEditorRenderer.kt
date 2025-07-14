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
import good.damn.engine.opengl.Object3D
import good.damn.engine.opengl.StaticMesh
import good.damn.engine.opengl.Vector
import good.damn.engine.opengl.files.CacheObjFile
import good.damn.engine.opengl.camera.RotationCamera
import good.damn.engine.opengl.entities.Landscape
import good.damn.engine.opengl.entities.SkySphere
import good.damn.engine.opengl.extensions.writeToFile
import good.damn.engine.opengl.OnMeshPositionListener
import good.damn.engine.opengl.light.DirectionalLight
import good.damn.engine.opengl.maps.DisplacementMap
import good.damn.engine.opengl.models.UserContent
import good.damn.engine.opengl.thread.GLHandler
import good.damn.engine.opengl.ui.GLButton
import good.damn.engine.touch.MGIListenerTransform
import good.damn.engine.touch.MGTouchScale
import good.damn.engine.utils.ShaderUtils
import java.io.FileInputStream
import java.util.LinkedList

class LevelEditorRenderer(
    private val requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer,
MGIListenerOnGetUserContent,
OnMeshPositionListener, MGIListenerTransform {

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

    private var mSelectedMesh: StaticMesh? = null

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

        mSky = SkySphere(
            mProgram,
            mCamera
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

        mLandscape.draw()
        meshes.forEach { editorMesh ->
            editorMesh.mesh!!.draw()
        }
        mSky.draw()
        mDirectionalLight.draw()
    }

    override fun onGetUserContent(
        userContent: UserContent
    ) {
        userContent.apply {
            if (extension.contains(
               "disp"
            )) {
                mLandscape.displace(
                    DisplacementMap.createFromStream(
                        stream
                    )
                )
                return
            }

            if (extension.contains("obj")) {
                // import obj
                val outFile = CacheObjFile(
                    System.currentTimeMillis().toString()
                )

                if (outFile.length() != 0L) {
                    return
                }

                stream.writeToFile(
                    outFile
                )

                val editorMesh = EditorMesh(
                    outFile,
                    "grass.jpg",
                    Vector(0f),
                    Vector(0f),
                    Vector(20f,20f,20f),
                    1,
                    1,
                )

                mHandler.post {
                    addMesh(
                        editorMesh
                    )
                }
                return
            }
        }
    }

    override fun onChangeMeshPosition(
        dx: Float,
        dy: Float,
        dz: Float
    ) {
        mSelectedMesh?.setPositionBy(
            dx,
            dy,
            dz
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

    private fun addMesh(
        editorMesh: EditorMesh
    ) {
        val mesh = StaticMesh(
            Object3D.createFromStream(
                FileInputStream(
                    editorMesh.objFile
                )
            ),
            "textures/${editorMesh.texName}",
            mProgram,
            mCamera
        )
        mesh.material.shine = 1f
        mesh.setPosition(
            editorMesh.position
        )
        mesh.setScale(
            editorMesh.scale
        )
        editorMesh.mesh = mesh
        meshes.add(
            editorMesh
        )

        mSelectedMesh = mesh
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