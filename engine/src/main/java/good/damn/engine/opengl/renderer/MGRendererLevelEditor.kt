package good.damn.engine.opengl.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import good.damn.engine.MGEngine
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.loaders.scripts.MGLoaderScripts
import good.damn.engine.loaders.texture.MGLoaderTextureAsync
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMInformatorShader
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerLightPass
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.executor.MGHandlerGlExecutor
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.framebuffer.MGFramebuffer
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.runnables.MGHudScene
import good.damn.engine.opengl.runnables.MGIRunnableBounds
import good.damn.engine.opengl.shaders.MGShaderLightPass
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.MGShaderBase
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.shaders.base.MGShaderSky
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox
import good.damn.engine.shader.MGShaderCache
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsFile
import good.damn.engine.utils.MGUtilsVertIndices
import java.util.concurrent.ConcurrentHashMap

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
        ),
        MGShaderSky(),
        MGShaderLightPass()
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

    private val mVerticesQuad = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mDrawerQuad = MGDrawerVertexArray(
        mVerticesQuad
    )

    private val mHandlerGlExecutor = MGHandlerGlExecutor()

    private val mHandlerGl = MGHandlerGl(
        mHandlerGlExecutor.queue,
        mHandlerGlExecutor.queueCycle,
    )

    private val mFramebufferG = MGFrameBufferG(
        MGFramebuffer()
    )

    private val mInformator = MGMInformator(
        mInformatorShader,
        MGCameraFree(
            MGMatrixTranslate()
        ),
        MGDrawerLightDirectional(),
        MGDrawerLightPass(
            mFramebufferG.textureAttachmentPosition.texture,
            mFramebufferG.textureAttachmentNormal.texture,
            mFramebufferG.textureAttachmentColorSpec.texture,
            mDrawerQuad
        ),
        ConcurrentHashMap(15),
        ConcurrentHashMap(50),
        MGSky(
            MGMaterialTexture.Builder()
                .buildTexture(
                    "sky.png",
                    MGEnumTextureType.DIFFUSE
                ).build(),
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
        mHandlerGl,
        MGLoaderTextureAsync(
            mHandlerGl
        ),
        true
    )

    private val mHudScene = MGHudScene(
        requesterUserContent,
        mInformator,
        mFramebufferG.framebuffer
    )

    init {
        mInformator.glHandler.post(
            object: MGIRunnableBounds {
                override fun run(
                    width: Int,
                    height: Int
                ) {
                    mFramebufferG.generate(
                        width, height
                    )
                    mHudScene.hud.layout(
                        width.toFloat(),
                        height.toFloat()
                    )
                }
            }
        )

        mInformator.glHandler.registerCycleTask(
            mHudScene.runnableCycle
        )
    }

    private var mWidth = 0
    private var mHeight = 0

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        MGUtilsFile.glWriteExtensions()

        mVerticesQuad.configure(
            MGUtilsBuffer.createFloat(
                MGUtilsVertIndices.createQuadVertices()
            ),
            MGUtilsBuffer.createByte(
                MGUtilsVertIndices.createQuadIndices()
            ),
            MGPointerAttribute.Builder()
                .pointPosition2()
                .pointTextureCoordinates()
                .build()
        )

        val bindUv = MGBinderAttribute.Builder()
            .bindPosition()
            .bindTextureCoordinates()
            .build()


        mInformatorShader.lightPass.setup(
            "shaders/post/vert.glsl",
            "shaders/opaque/defer/frag_defer_light.glsl",
            MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .build()
        )

        mInformatorShader.sky.setup(
            "shaders/diffuse/vert.glsl",
            "shaders/diffuse/frag_sky.glsl",
            bindUv
        )

        setupShaders(
            mInformatorShader.map,
            "shaders/diffuse",
            binderAttributeSingle = bindUv,
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

        mInformator.meshSky.configure(
            mPoolTextures
        )

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

        glDepthFunc(
            GL_LESS
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

        mInformator.camera.setPerspective(
            width,
            height
        )

        mWidth = width
        mHeight = height
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        mHandlerGlExecutor.runTasksBounds(
            mWidth, mHeight
        )

        mHandlerGlExecutor.runCycle(
            mWidth, mHeight
        )
    }

    fun onTouchEvent(
        event: MotionEvent
    ) = mHudScene.hud.touchEvent(
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