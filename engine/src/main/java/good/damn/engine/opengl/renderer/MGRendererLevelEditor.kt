package good.damn.engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.util.Log
import android.view.MotionEvent
import good.damn.engine.MGEngine
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.MGObject3D
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.callbacks.MGCallbackOnCameraMovement
import good.damn.engine.opengl.callbacks.MGCallbackOnDeltaInteract
import good.damn.engine.opengl.callbacks.MGIListenerOnIntersectPosition
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeOpaque
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSingleShader
import good.damn.engine.opengl.drawers.sky.MGDrawerSkyOpaque
import good.damn.engine.opengl.entities.MGLight
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.generators.MGGeneratorLandscape
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.maps.MGMapNormal
import good.damn.engine.opengl.iterators.vertex.MGVertexIteratorLandscapeDisplace
import good.damn.engine.opengl.iterators.vertex.MGVertexIteratorLandscapeNormal
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.models.MGMDrawMode
import good.damn.engine.opengl.scene.MGScene
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.MGDrawerTriggerStateable
import good.damn.engine.opengl.triggers.MGManagerTriggerState
import good.damn.engine.opengl.triggers.MGMatrixTriggerMesh
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.engine.touch.MGIListenerScale
import good.damn.engine.ui.MGUILayerEditor
import good.damn.engine.ui.clicks.MGClickGenerateLandscape
import good.damn.engine.ui.clicks.MGClickPlaceMesh
import good.damn.engine.ui.clicks.MGClickSwitchDrawMode
import good.damn.engine.utils.MGUtilsAlgo
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsVertIndices
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.cos
import kotlin.math.sin

class MGRendererLevelEditor(
    requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mShaderDefault = MGShaderDefault()
    private val mShaderSky = MGShaderSkySphere()
    private val mShaderNormals = MGShaderSingleModeNormals()
    private val mShaderTexCoords = MGShaderSingleMode()
    private val mShaderWireframe = MGShaderSingleMode()

    private var mWidth = 0
    private var mHeight = 0

    private val mSceneTest = MGScene(
        requesterUserContent,
        mShaderDefault,
        mShaderSky,
        mShaderNormals,
        mShaderTexCoords,
        mShaderWireframe
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
        mShaderWireframe.setup(
            "shaders/wireframe/vert.glsl",
            "shaders/wireframe/frag.glsl"
        )

        mShaderDefault.setup(
            "shaders/vert.glsl",
            "shaders/frag.glsl"
        )

        mShaderSky.setup(
            "shaders/sky/vert.glsl",
            "shaders/sky/frag.glsl"
        )

        mShaderNormals.setup(
            "shaders/normals/vert.glsl",
            "shaders/normals/frag.glsl"
        )

        mShaderTexCoords.setup(
            "shaders/texCoords/vert.glsl",
            "shaders/texCoords/frag.glsl"
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
            Log.d("TAG", "onDrawFrame: ERROR: ${error.toString(16)}")
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