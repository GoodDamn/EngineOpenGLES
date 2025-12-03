package good.damn.engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.opengl.GLU
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import good.damn.engine.MGEngine
import good.damn.engine.hud.MGHud
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.loaders.scripts.MGLoaderScripts
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMInformatorShader
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.base.MGShaderBase
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox
import good.damn.engine.sdk.MGVector3
import good.damn.engine.shader.MGShaderCache
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsFile
import good.damn.engine.utils.MGUtilsVertIndices
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class MGRendererLevelEditor(
    requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mInformatorShader = MGMInformatorShader(
        MGEngine.shaderSource,
        MGShaderCache(
            SparseArray(5)
        ),
        MGShaderCache(
            SparseArray(5)
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

    private val mPoolTextures = MGPoolTextures()

    private val managerLight = MGManagerLight(
        MGMInformatorShader.SIZE_LIGHT_POINT
    )

    private val mVerticesBox = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mDrawerBox = MGDrawerVertexArray(
        mVerticesBox
    )

    private val mInformator = MGMInformator(
        mInformatorShader,
        MGCameraFree(
            MGMatrixTranslate()
        ),
        MGDrawerLightDirectional(),
        ConcurrentHashMap(15),
        ConcurrentHashMap(50),
        MGSky(
            MGTexture(
                MGEnumTextureType.DIFFUSE
            ),
            MGArrayVertexConfigurator(
                MGEnumArrayVertexConfiguration.SHORT
            )
        ),
        managerLight,
        MGManagerTriggerLight(
            managerLight,
            mDrawerBox
        ),
        MGManagerTriggerMesh(
            mDrawerBox
        ),
        mPoolTextures,
        MGPoolMeshesStatic(),
        MGHandlerGl(),
        true
    )

    private val mSwitcherDrawMode = MGSwitcherDrawMode(
        mInformator
    )

    private val mHud = MGHud(
        requesterUserContent,
        mInformator,
        mSwitcherDrawMode
    )

    private var mWidth = 0
    private var mHeight = 0
    
    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        MGUtilsFile.glWriteExtensions()

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

        /*setupShaders(
            mInformatorShader.opaque,
            "shaders/opaque",
            binderAttributeSingle = MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .bindNormal()
                .bindTangent()
                .build(),
            binderAttributeInstanced = MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .bindNormal()
                .bindInstancedModel()
                .bindInstancedRotationMatrix()
                .bindTangent()
                .build()
        )*/

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

        mInformator.meshSky.configure()
        mVerticesBox.configure(
            MGUtilsBuffer.createFloat(
                MGUtilsVertIndices.createCubeVertices(
                    MGTriggerMethodBox.MIN,
                    MGTriggerMethodBox.MAX
                )
            ),
            MGUtilsBuffer.createByte(
                MGUtilsVertIndices.createCubeIndices()
            ),
            MGPointerAttribute.Builder()
                .pointPosition()
                .build()
        )

        mInformator.camera.run {
            modelMatrix.setPosition(
                0f, 0f, 0f
            )
            modelMatrix.invalidatePosition()
        }

        MGLoaderScripts.executeDirLight(
            mInformator.drawerLightDirectional
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
        mInformator.camera.setPerspective(
            width,
            height
        )

        mHud.layout(
            width.toFloat(),
            height.toFloat()
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

        mInformator
            .glHandler
            .run()

        mSwitcherDrawMode
            .currentDrawerMode
            .draw()
    }

    fun onTouchEvent(
        event: MotionEvent
    ) = mHud.touchEvent(
        event
    )

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
}