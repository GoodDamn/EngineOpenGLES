package good.damn.engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.opengl.GLU
import android.util.Log
import android.view.MotionEvent
import good.damn.engine.MGEngine
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.scene.MGScene
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.utils.MGUtilsAsset
import good.damn.engine.utils.MGUtilsFile
import java.io.File

class MGRendererLevelEditor(
    requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mShaderOpaque = MGMShader(
        MGShaderDefault(),
        MGShaderOpaque()
    )

    private val mShaderSky = MGShaderSkySphere()
    private val mShaderNormals = MGShaderSingleModeNormals()

    private val mShaderTexCoords = MGMShader(
        MGShaderSingleMode(),
        MGShaderSingleModeInstanced()
    )

    private val mShaderWireframe = MGMShader(
        MGShaderSingleMode(),
        MGShaderSingleModeInstanced()
    )

    private val mShaderMapEmissive = MGMShader(
        MGShaderSingleMap(),
        MGShaderSingleMapInstanced()
    )

    private var mWidth = 0
    private var mHeight = 0

    private val mSceneTest = MGScene(
        requesterUserContent,
        mShaderOpaque,
        mShaderSky,
        mShaderNormals,
        mShaderTexCoords,
        mShaderWireframe,
        mShaderMapEmissive
    )
    
    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        File(
            MGEngine.DIR_PUBLIC,
            "extensions.txt"
        ).run {
            if (length() != 0L) {
                return@run
            }

            if (!exists() && createNewFile()) {
                Log.d(TAG, "onSurfaceCreated: $name is created")
            }

            val extensions = glGetString(
                GL_EXTENSIONS
            ).replace(" ".toRegex(), "\n")

            val numExt = extensions.count {
                it == '\n'
            }

            val vendor = glGetString(
                GL_VENDOR
            )

            val renderer = glGetString(
                GL_RENDERER
            )

            val version = glGetString(
                GL_VERSION
            )

            outputStream().run {
                write(
                    numExt.toString().encodeToByteArray()
                )
                write(10)
                write(
                    extensions.encodeToByteArray()
                )

                write(10)
                write(10)

                write(
                    renderer.encodeToByteArray()
                )
                write(10)

                write(
                    vendor.encodeToByteArray()
                )
                write(10)

                write(
                    version.encodeToByteArray()
                )
                close()
            }

        }

        mShaderMapEmissive.run {
            single.setup(
                "shaders/metallic/vert.glsl",
                "shaders/metallic/frag.glsl"
            )

            instanced.setup(
                "shaders/metallic/vert_i.glsl",
                "shaders/metallic/frag.glsl"
            )
        }

        mShaderWireframe.run {
            single.setup(
                "shaders/wireframe/vert.glsl",
                "shaders/wireframe/frag.glsl"
            )

            instanced.setup(
                "shaders/wireframe/vert_i.glsl",
                "shaders/wireframe/frag.glsl"
            )
        }

        mShaderOpaque.run {
            single.setup(
                "shaders/vert.glsl",
                "shaders/frag.glsl"
            )

            instanced.setup(
                "shaders/vert_i.glsl",
                "shaders/frag.glsl"
            )
        }

        mShaderTexCoords.run {
            single.setup(
                "shaders/texCoords/vert.glsl",
                "shaders/texCoords/frag.glsl"
            )
            instanced.setup(
                "shaders/texCoords/vert_i.glsl",
                "shaders/texCoords/frag.glsl"
            )
        }

        mShaderSky.setup(
            "shaders/sky/vert.glsl",
            "shaders/sky/frag.glsl"
        )

        mShaderNormals.setup(
            "shaders/normals/vert.glsl",
            "shaders/normals/frag.glsl"
        )


        mSceneTest.onSurfaceCreated(
            gl, config
        )

        glEnable(
            GL_DEPTH_TEST
        )

        glEnable(
            GL_CULL_FACE
        )

        glCullFace(
            GL_FRONT
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
        mSceneTest.onSurfaceChanged(
            gl,
            width,
            height
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
            0.0f,
            0.0f,
            0.0f,
            1.0f
        )

        val error = glGetError()
        if (error != GL_NO_ERROR) {
            Log.d("TAG", "onDrawFrame: ERROR: ${error.toString(16)}: ${GLU.gluErrorString(error)}")
            return
        }

        mSceneTest.onDrawFrame(
            gl
        )
    }

    fun onTouchEvent(
        event: MotionEvent
    ) {
        mSceneTest.onTouchEvent(
            event
        )
    }
}