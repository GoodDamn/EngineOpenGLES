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
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.buffers.MGBuffer
import good.damn.engine.opengl.buffers.MGBufferUniform
import good.damn.engine.opengl.buffers.MGBufferUniformCamera
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.executor.MGHandlerGlExecutor
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.framebuffer.MGFramebuffer
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.managers.MGManagerVolume
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.models.MGMLightPass
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.pools.MGPoolMaterials
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.runnables.MGHudScene
import good.damn.engine.opengl.runnables.MGIRunnableBounds
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.lightpass.MGShaderLightPass
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.shaders.creators.MGShaderCreatorGeomPassInstanced
import good.damn.engine.opengl.shaders.creators.MGShaderCreatorGeomPassModel
import good.damn.engine.opengl.shaders.lightpass.MGShaderLightPassPointLight
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox
import good.damn.engine.runnables.MGManagerProcessTime
import good.damn.engine.shader.MGShaderCache
import good.damn.engine.shader.MGShaderSource
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsFile
import good.damn.engine.utils.MGUtilsVertIndices
import java.util.concurrent.ConcurrentLinkedQueue

class MGRendererLevelEditor(
    requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    init {
        MGEngine.shaderSource = MGShaderSource(
            "opaque"
        )
    }

    private val mHandlerGlExecutor = MGHandlerGlExecutor()

    private val mHandlerGl = MGHandlerGl(
        mHandlerGlExecutor.queue,
        mHandlerGlExecutor.queueCycle,
    )

    private val mPoolTextures = MGPoolTextures(
        MGLoaderTextureAsync(
            mHandlerGl
        )
    )

    private val mInformatorShader = MGMInformatorShader(
        MGEngine.shaderSource,
        cacheGeometryPass = MGShaderCache(
            SparseArray(50),
            mHandlerGl,
            MGShaderCreatorGeomPassModel()
        ),
        cacheGeometryPassInstanced = MGShaderCache(
            SparseArray(50),
            mHandlerGl,
            MGShaderCreatorGeomPassInstanced()
        ),
        wireframe = MGShaderGeometryPassModel(
            MGShaderMaterial.empty
        ),
        lightPasses = arrayOf(
            MGMLightPass(
                MGShaderLightPass.Builder()
                    .attachAll()
                    .build(),
                "shaders/lightPass/vert.glsl",
                "shaders/opaque/defer/frag_defer_light_dir.glsl",
            ),
            MGMLightPass(
                MGShaderLightPass.Builder()
                    .attachColorSpec()
                    .build(),
                "shaders/lightPass/vert.glsl",
                "shaders/lightPass/frag_defer.glsl"
            ),
            MGMLightPass(
                MGShaderLightPass.Builder()
                    .attachDepth()
                    .build(),
                "shaders/lightPass/vert.glsl",
                "shaders/lightPass/frag_defer_depth.glsl"
            ),
            MGMLightPass(
                MGShaderLightPass.Builder()
                    .attachNormal()
                    .build(),
                "shaders/lightPass/vert.glsl",
                "shaders/lightPass/frag_defer_normal.glsl"
            )
        ),
        MGShaderLightPassPointLight.Builder()
            .attachAll()
            .build()
    )

    private val mVerticesBox = MGArrayVertexManager(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mDrawerBox = MGDrawerVertexArray(
        mVerticesBox
    )

    private val mVerticesQuad = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mBufferUniformCamera = MGBufferUniformCamera(
        MGBuffer(
            GL_UNIFORM_BUFFER
        )
    )

    private val managerLight = MGManagerLight(
        mDrawerBox
    )

    private val mCameraFree = MGCameraFree(
        mBufferUniformCamera,
        MGMatrixTranslate()
    )

    private val mInformator = MGMInformator(
        mInformatorShader,
        mCameraFree,
        MGDrawerLightDirectional(),
        MGDrawerVertexArray(
            mVerticesQuad
        ),
        ConcurrentLinkedQueue(),
        ConcurrentLinkedQueue(),
        MGFrameBufferG(
            MGFramebuffer()
        ),
        meshSky = MGSky(),
        managerLight,
        MGManagerVolume(
            mCameraFree,
            mDrawerBox
        ),
        MGManagerTriggerMesh(
            mDrawerBox
        ),
        MGManagerProcessTime(),
        mPoolTextures,
        MGPoolMeshesStatic(),
        MGPoolMaterials(),
        mHandlerGl,
        true
    )

    private val mHudScene = MGHudScene(
        requesterUserContent,
        mInformator,
        mInformator.framebufferG
    )

    init {
        mInformator.glHandler.post(
            object: MGIRunnableBounds {
                override fun run(
                    width: Int,
                    height: Int
                ) {
                    mInformator.framebufferG.generate(
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

    fun stop() {
        mInformator.managerProcessTime.run {
            stop()
            unregisterAll()
        }
    }

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        MGUtilsFile.glWriteExtensions()

        mBufferUniformCamera.apply {
            buffer.generate()
            MGBufferUniform.setupBindingPoint(
                0,
                buffer,
                2 * 64
            )
        }

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

        mInformatorShader.wireframe.setup(
            "shaders/opaque/vert.glsl",
            "shaders/wireframe/frag_defer.glsl",
            MGBinderAttribute.Builder()
                .bindPosition()
                .build()
        )

        MGBinderAttribute.Builder()
            .bindPosition()
            .bindTextureCoordinates()
            .build().run {
                mInformatorShader.lightPasses.forEach {
                    it.shader.setup(
                        it.vertPath,
                        it.fragPath,
                        this
                    )
                }

                mInformatorShader.lightPassPointLight.setup(
                    "shaders/lightPass/vert_pointLight.glsl",
                    "shaders/opaque/defer/frag_defer_light_point.glsl",
                    this
                )
            }

        mInformator.meshSky.configure(
            mInformator
        )

        val pointPosition = MGPointerAttribute.Builder()
            .pointPosition()
            .build()

        /*MGObject3d.createFromAssets(
            "objs/semi_sphere.obj"
        )?.get(0)?.let {
            mVerticesSphere.configure(
                it.vertices,
                it.indices,
                pointPosition
            )
        }*/


        val bufferVertices = MGUtilsBuffer.createFloat(
            MGUtilsVertIndices.createCubeVertices(
                MGTriggerMethodBox.MIN,
                MGTriggerMethodBox.MAX
            )
        )

        mVerticesBox.configure(
            bufferVertices,
            MGUtilsBuffer.createByte(
                MGUtilsVertIndices.createCubeIndices()
            ),
            pointPosition
        )

        mVerticesBox.keepBufferVertices(
            bufferVertices
        )

        mInformator.managerLightVolumes.loadPositions(
            mVerticesBox
        )

        mVerticesBox.unkeepBufferVertices()

        mInformator.managerProcessTime.run {
            /*registerLoopProcessTime(
                mInformator.managerLightVolumes
            )*/
            start()
        }

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
            height,
            mInformator.glHandler
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
}