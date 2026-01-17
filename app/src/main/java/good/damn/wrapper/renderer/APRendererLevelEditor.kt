package good.damn.wrapper.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import good.damn.common.COHandlerGl
import good.damn.common.COIRunnableBounds
import good.damn.wrapper.interfaces.MGIRequestUserContent
import good.damn.engine.loaders.texture.MGLoaderTextureAsync
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMInformatorShader
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.common.vertex.COMArrayVertexManager
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.buffers.MGBuffer
import good.damn.engine.opengl.buffers.MGBufferUniform
import good.damn.engine.opengl.buffers.MGBufferUniformCamera
import good.damn.common.camera.COCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.common.COHandlerGlExecutor
import good.damn.common.camera.COCameraProjection
import good.damn.common.camera.COMCamera
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.framebuffer.MGFramebuffer
import good.damn.engine.opengl.managers.MGDrawerLights
import good.damn.logic.triggers.managers.MGManagerTriggerMesh
import good.damn.common.matrices.COMatrixTranslate
import good.damn.common.volume.COManagerFrustrum
import good.damn.engine.opengl.models.MGMLightPass
import good.damn.engine.MGObject3d
import good.damn.logic.pools.MGPoolMaterials
import good.damn.logic.pools.MGPoolMeshesStatic
import good.damn.logic.pools.MGPoolTextures
import good.damn.wrapper.hud.APHudScene
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.lightpass.MGShaderLightPass
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.shaders.creators.MGShaderCreatorGeomPassInstanced
import good.damn.engine.opengl.shaders.creators.MGShaderCreatorGeomPassModel
import good.damn.engine.opengl.shaders.lightpass.MGShaderLightPassPointLight
import good.damn.engine.camera.GLCameraFree
import good.damn.engine.camera.GLCameraProjection
import good.damn.engine.opengl.drawers.volume.MGDrawerVolumes
import good.damn.logic.triggers.methods.MGTriggerMethodBox
import good.damn.logic.process.MGManagerProcessTime
import good.damn.engine.sdk.SDVector3
import good.damn.engine.shader.MGShaderCache
import good.damn.engine.shader.MGShaderSource
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsFile
import good.damn.engine.utils.MGUtilsVertIndices
import good.damn.script.SCLoaderScripts
import java.util.concurrent.ConcurrentLinkedQueue

class APRendererLevelEditor(
    requesterUserContent: MGIRequestUserContent
): GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mHandlerGlExecutor = COHandlerGlExecutor()

    private val mBuffer = ByteArray(
        1024 * 50
    )

    private val mHandlerGl = COHandlerGl(
        mHandlerGlExecutor.queue,
        mHandlerGlExecutor.queueCycle,
    )

    private val mPoolTextures = good.damn.logic.pools.MGPoolTextures(
        MGLoaderTextureAsync(
            mHandlerGl
        )
    )

    private val mInformatorShader = MGMInformatorShader(
        MGShaderSource(
            "opaque",
            mBuffer
        ),
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

    private val mVerticesSphere = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.SHORT
    )

    private val mVerticesBox05 = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mVerticesBox10 = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val verticesBox10Raw = MGUtilsBuffer.createFloat(
        MGUtilsVertIndices.createCubeVertices(
            SDVector3(
                -1.0f, -1.0f, -1.0f
            ),
            SDVector3(
                1.0f, 1.0f, 1.0f
            )
        )
    )

    private val mDrawerSphere = MGDrawerVertexArray(
        mVerticesSphere
    )

    private val mDrawerBox05 = MGDrawerVertexArray(
        mVerticesBox05
    )

    private val mDrawerBox10 = MGDrawerVertexArray(
        mVerticesBox10
    )

    private val mVerticesQuad = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mBufferUniformCamera = MGBufferUniformCamera(
        MGBuffer(
            GL_UNIFORM_BUFFER
        )
    )

    private val managerLight = MGDrawerLights(
        mDrawerSphere
    )

    private val mCameraFree = COMatrixTranslate().run {
        COMCamera(
            GLCameraFree(
                COCameraFree(
                    this
                ),
                mHandlerGl,
                mBufferUniformCamera
            ),
            GLCameraProjection(
                COCameraProjection(
                    this
                ),
                mHandlerGl,
                mBufferUniformCamera
            )
        )
    }

    private val managerFrustrum = COManagerFrustrum(
        mCameraFree.projection,
        COMArrayVertexManager(
            verticesBox10Raw
        )
    )

    private val mInformator = MGMInformator(
        mInformatorShader,
        mCameraFree,
        MGDrawerLightDirectional(),
        MGDrawerVertexArray(
            mVerticesQuad
        ),
        MGDrawerVolumes(
            mDrawerBox10,
            managerFrustrum
        ),
        ConcurrentLinkedQueue(),
        ConcurrentLinkedQueue(),
        MGFrameBufferG(
            MGFramebuffer()
        ),
        meshSky = MGSky(),
        managerLight,
        managerFrustrum,
        good.damn.logic.triggers.managers.MGManagerTriggerMesh(
            mDrawerBox05
        ),
        good.damn.logic.process.MGManagerProcessTime(),
        mPoolTextures,
        good.damn.logic.pools.MGPoolMeshesStatic(),
        good.damn.logic.pools.MGPoolMaterials(),
        mHandlerGl,
        true
    )

    private val mHudScene = APHudScene(
        requesterUserContent,
        mInformator,
        mInformator.framebufferG
    )

    init {
        mInformator.glHandler.post(
            object: COIRunnableBounds {
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
                sizeBytes
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
            mBuffer,
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
                        mBuffer,
                        it.vertPath,
                        it.fragPath,
                        this
                    )
                }

                mInformatorShader.lightPassPointLight.setup(
                    mBuffer,
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

        MGObject3d.createFromAssets(
            "objs/sphere.fbx"
        )?.get(0)?.let {
            mVerticesSphere.configure(
                it.vertices,
                it.indices,
                MGPointerAttribute.Builder()
                    .pointPosition()
                    .pointTextureCoordinates()
                    .pointNormal()
                    .build()
            )
        }

        val indicesBox = MGUtilsBuffer.createByte(
            MGUtilsVertIndices.createCubeIndices()
        )

        mVerticesBox05.configure(
            MGUtilsBuffer.createFloat(
                MGUtilsVertIndices.createCubeVertices(
                    good.damn.logic.triggers.methods.MGTriggerMethodBox.MIN,
                    good.damn.logic.triggers.methods.MGTriggerMethodBox.MAX
                )
            ),
            indicesBox,
            pointPosition
        )


        mVerticesBox10.configure(
            verticesBox10Raw,
            indicesBox,
            pointPosition
        )

        mInformator.managerProcessTime.run {
            registerLoopProcessTime(
                mInformator.managerLightVolumes
            )
            start()
        }

        mInformator.camera.projection.run {
            modelMatrix.setPosition(
                0f, 0f, 0f
            )
            modelMatrix.invalidatePosition()
        }

        SCLoaderScripts.executeDirLight(
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

        mInformator.camera.projection.setPerspective(
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
}