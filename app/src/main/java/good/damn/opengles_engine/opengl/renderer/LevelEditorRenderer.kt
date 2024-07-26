package good.damn.opengles_engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.util.Log
import good.damn.opengles_engine.App
import good.damn.opengles_engine.activities.LevelEditorActivity
import good.damn.opengles_engine.opengl.EditorMesh
import good.damn.opengles_engine.opengl.Object3D
import good.damn.opengles_engine.opengl.StaticMesh
import good.damn.opengles_engine.opengl.Vector
import good.damn.opengles_engine.opengl.cache.files.CacheObjFile
import good.damn.opengles_engine.opengl.camera.RotationCamera
import good.damn.opengles_engine.opengl.entities.Landscape
import good.damn.opengles_engine.opengl.entities.SkySphere
import good.damn.opengles_engine.opengl.extensions.writeToFile
import good.damn.opengles_engine.opengl.interfaces.OnGetUserContentUri
import good.damn.opengles_engine.opengl.light.DirectionalLight
import good.damn.opengles_engine.opengl.maps.DisplacementMap
import good.damn.opengles_engine.opengl.models.UserContent
import good.damn.opengles_engine.opengl.thread.GLHandler
import good.damn.opengles_engine.opengl.ui.GLButton
import good.damn.opengles_engine.utils.ShaderUtils
import java.io.File
import java.io.FileInputStream
import java.util.LinkedList

class LevelEditorRenderer(
    var context: LevelEditorActivity?
): GLSurfaceView.Renderer,
OnGetUserContentUri {

    companion object {
        private const val TAG = "LevelEditorRenderer"
    }

    private val mHandler = GLHandler()

    private val meshes = LinkedList<EditorMesh>()

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

    private val mBtnLoadUserContent = GLButton {
        context?.loadFromUserDisk(
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
    private lateinit var mSky: SkySphere

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

        mBtnLoadUserContent.bounds(
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

        mHandler.run()

        mLandscape.draw()
        meshes.forEach { editorMesh ->
            editorMesh.mesh!!.draw()
        }
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
            mBtnLoadUserContent.intercept(x,y)
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

    override fun onGetUserContentUri(
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
    }
}