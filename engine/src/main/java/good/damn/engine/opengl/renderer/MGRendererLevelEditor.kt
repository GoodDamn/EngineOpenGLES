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
import good.damn.engine.models.MGMInformatorShader
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.scene.MGScene
import good.damn.engine.opengl.shaders.base.MGShaderBase
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import java.io.File

class MGRendererLevelEditor(
    requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mInformatorShader = MGMInformatorShader(
        MGMShader(
            MGShaderDefault(),
            MGShaderOpaque()
        ),
        MGMShader(
            MGShaderSingleMode(),
            MGShaderSingleModeInstanced()
        ),
        MGMShader(
            MGShaderSingleModeNormals(),
            MGShaderSingleModeInstanced(),
        ),
        MGMShader(
            MGShaderSingleMode(),
            MGShaderSingleModeInstanced()
        ),
        MGMShader(
            MGShaderSingleMap(),
            MGShaderSingleMapInstanced()
        )
    )

    private var mWidth = 0
    private var mHeight = 0

    private val mSceneTest = MGScene()
    
    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        writeExtensions()

        setupShaders(
            mInformatorShader.map,
            "shaders/diffuse",
            binderAttributeSingle = MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .build(),
            binderAttributeInstanced = MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .bindInstancedModel()
                .build()
        )

        setupShaders(
            mInformatorShader.wireframe,
            "shaders/wireframe",
            binderAttributeSingle = MGBinderAttribute.Builder()
                .bindPosition()
                .build(),
            binderAttributeInstanced = MGBinderAttribute.Builder()
                .bindPosition()
                .bindInstancedModel()
                .build()
        )

        setupShaders(
            mInformatorShader.opaque,
            "shaders/",
            binderAttributeSingle = MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .bindNormal()
                .build(),
            binderAttributeInstanced = MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .bindNormal()
                .bindInstancedModel()
                .bindInstancedRotationMatrix()
                .build()
        )

        setupShaders(
            mInformatorShader.texCoords,
            "shaders/texCoords",
            binderAttributeSingle = MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .build(),
            binderAttributeInstanced = MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .bindInstancedModel()
                .build()
        )

        setupShaders(
            mInformatorShader.normals,
            "shaders/normals",
            binderAttributeSingle = MGBinderAttribute.Builder()
                .bindPosition()
                .bindNormal()
                .build(),
            binderAttributeInstanced = MGBinderAttribute.Builder()
                .bindPosition()
                .bindNormal()
                .bindInstancedModel()
                .bindInstancedRotationMatrix()
                .build()
        )

        mSceneTest.onSurfaceCreated(
            gl, config
        )

        glEnable(
            GL_DEPTH_TEST
        )

        glDepthFunc(
            GL_LESS
        )

        glEnable(
            GL_CULL_FACE
        )

        glCullFace(
            GL_BACK
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

        glClear(
            GL_COLOR_BUFFER_BIT or
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

    private inline fun <
        T: MGShaderBase,
        M: MGShaderBase
    > setupShaders(
        shader: MGMShader<T, M>,
        localPath: String,
        binderAttributeSingle: MGBinderAttribute,
        binderAttributeInstanced: MGBinderAttribute
    ) {
        val pathFragment = "$localPath/frag.glsl"
        shader.single.setup(
            "$localPath/vert.glsl",
            pathFragment,
            binderAttributeSingle
        )

        shader.instanced.setup(
            "$localPath/vert_i.glsl",
            pathFragment,
            binderAttributeInstanced
        )
    }

    private inline fun writeExtensions() {
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

            val glslVersion = glGetString(
                GL_SHADING_LANGUAGE_VERSION
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

                write(10)
                write(
                    glslVersion.encodeToByteArray()
                )

                close()
            }

        }
    }
}