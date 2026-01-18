package good.damn.wrapper.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.pointers.GLPointerAttribute
import good.damn.apigl.buffers.GLBuffer
import good.damn.apigl.buffers.GLBufferUniform
import good.damn.apigl.buffers.GLBufferUniformCamera
import good.damn.apigl.drawers.GLDrawerLightDirectional
import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.drawers.GLDrawerLights
import good.damn.apigl.drawers.GLDrawerVertexArray
import good.damn.apigl.drawers.GLDrawerVolumes
import good.damn.apigl.enums.GLEnumArrayVertexConfiguration
import good.damn.apigl.framebuffer.GLFrameBufferG
import good.damn.apigl.framebuffer.GLFramebuffer
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.apigl.shaders.GLShaderMaterial
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.apigl.shaders.creators.GLShaderCreatorGeomPassInstanced
import good.damn.apigl.shaders.creators.GLShaderCreatorGeomPassModel
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.apigl.shaders.lightpass.GLShaderLightPassPointLight
import good.damn.common.COHandlerGl
import good.damn.common.COIRunnableBounds
import good.damn.wrapper.interfaces.MGIRequestUserContent
import good.damn.engine.loaders.texture.MGLoaderTextureAsync
import good.damn.engine.models.MGMInformatorShader
import good.damn.common.vertex.COMArrayVertexManager
import good.damn.common.camera.COCameraFree
import good.damn.common.COHandlerGlExecutor
import good.damn.common.camera.COCameraProjection
import good.damn.common.camera.COMCamera
import good.damn.common.matrices.COMatrixTranslate
import good.damn.common.volume.COManagerFrustrum
import good.damn.engine.opengl.models.MGMLightPass
import good.damn.engine.MGObject3d
import good.damn.wrapper.hud.APHudScene
import good.damn.engine.camera.GLCameraFree
import good.damn.engine.camera.GLCameraProjection
import good.damn.engine.opengl.MGMGeometry
import good.damn.engine.opengl.MGMVolume
import good.damn.engine.opengl.MGSky
import good.damn.engine.opengl.pools.MGPoolMaterials
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.sdk.SDVector3
import good.damn.engine.shader.MGShaderCache
import good.damn.engine.shader.MGShaderSource
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsFile
import good.damn.engine.utils.MGUtilsVertIndices
import good.damn.logic.process.LGManagerProcessTime
import good.damn.logic.triggers.managers.LGManagerTriggerMesh
import good.damn.logic.triggers.methods.LGTriggerMethodBox
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

    private val mPoolTextures = MGPoolTextures(
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
            GLShaderCreatorGeomPassModel()
        ),
        cacheGeometryPassInstanced = MGShaderCache(
            SparseArray(50),
            mHandlerGl,
            GLShaderCreatorGeomPassInstanced()
        ),
        wireframe = GLShaderGeometryPassModel(
            GLShaderMaterial.empty
        ),
        lightPasses = arrayOf(
            MGMLightPass(
                GLShaderLightPass.Builder()
                    .attachAll()
                    .build(),
                "shaders/lightPass/vert.glsl",
                "shaders/opaque/defer/frag_defer_light_dir.glsl",
            ),
            MGMLightPass(
                GLShaderLightPass.Builder()
                    .attachColorSpec()
                    .build(),
                "shaders/lightPass/vert.glsl",
                "shaders/lightPass/frag_defer.glsl"
            ),
            MGMLightPass(
                GLShaderLightPass.Builder()
                    .attachDepth()
                    .build(),
                "shaders/lightPass/vert.glsl",
                "shaders/lightPass/frag_defer_depth.glsl"
            ),
            MGMLightPass(
                GLShaderLightPass.Builder()
                    .attachNormal()
                    .build(),
                "shaders/lightPass/vert.glsl",
                "shaders/lightPass/frag_defer_normal.glsl"
            )
        ),
        GLShaderLightPassPointLight.Builder()
            .attachAll()
            .build()
    )

    private val mVerticesSphere = GLArrayVertexConfigurator(
        GLEnumArrayVertexConfiguration.SHORT
    )

    private val mVerticesBox05 = GLArrayVertexConfigurator(
        GLEnumArrayVertexConfiguration.BYTE
    )

    private val mVerticesBox10 = GLArrayVertexConfigurator(
        GLEnumArrayVertexConfiguration.BYTE
    )

    private val mVerticesQuad = GLArrayVertexConfigurator(
        GLEnumArrayVertexConfiguration.BYTE
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

    private val mDrawerBox05 = GLDrawerVertexArray(
        mVerticesBox05
    )

    private val mDrawerBox10 = GLDrawerVertexArray(
        mVerticesBox10
    )

    private val mBufferUniformCamera = GLBufferUniformCamera(
        GLBuffer(
            GL_UNIFORM_BUFFER
        )
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

    private val mFramebufferG = GLFrameBufferG(
        GLFramebuffer()
    )

    private val mDrawerVolumes = GLDrawerVolumes(
        mDrawerBox10,
        managerFrustrum
    )

    private val mDrawerLightDirectional = GLDrawerLightDirectional()

    private val mGeometry = MGMGeometry(
        ConcurrentLinkedQueue(),
        ConcurrentLinkedQueue(),
        MGSky(),
        GLDrawerVertexArray(
            mVerticesQuad
        ),
        GLDrawerVertexArray(
            mVerticesSphere
        )
    )

    private val mVolume = MGMVolume(
        mDrawerVolumes
    )

    private val managerTrigger = LGManagerTriggerMesh()

    private val managerProcessTime = LGManagerProcessTime()

    private val mPoolMeshes = MGPoolMeshesStatic(
        mHandlerGl
    )

    private val mPoolMaterials = MGPoolMaterials()

    private val mHudScene = APHudScene(
        requesterUserContent,
        mFramebufferG,
        mInformatorShader,
        mGeometry,
        mVolume,
        mDrawerLightDirectional
    )

    init {
        mHandlerGl.post(
            object: COIRunnableBounds {
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

        mHandlerGl.registerCycleTask(
            mHudScene.runnableCycle
        )
    }

    private var mWidth = 0
    private var mHeight = 0

    fun stop() {
        managerProcessTime.apply {
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
            GLBufferUniform.setupBindingPoint(
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
            GLPointerAttribute.Builder()
                .pointPosition2()
                .pointTextureCoordinates()
                .build()
        )

        mInformatorShader.wireframe.setup(
            mBuffer,
            "shaders/opaque/vert.glsl",
            "shaders/wireframe/frag_defer.glsl",
            GLBinderAttribute.Builder()
                .bindPosition()
                .build()
        )

        GLBinderAttribute.Builder()
            .bindPosition()
            .bindTextureCoordinates()
            .build().apply {
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

        mGeometry.meshSky.configure(
            mInformatorShader,
            mPoolTextures
        )

        val pointPosition = GLPointerAttribute.Builder()
            .pointPosition()
            .build()

        MGObject3d.createFromAssets(
            "objs/sphere.fbx"
        )?.get(0)?.let {
            mVerticesSphere.configure(
                it.vertices,
                it.indices,
                GLPointerAttribute.Builder()
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
                    LGTriggerMethodBox.MIN,
                    LGTriggerMethodBox.MAX
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

        managerProcessTime.apply {
            registerLoopProcessTime(
                managerFrustrum
            )
            start()
        }

        mCameraFree.projection.run {
            modelMatrix.setPosition(
                0f, 0f, 0f
            )
            modelMatrix.invalidatePosition()
        }

        SCLoaderScripts.executeDirLight(
            mDrawerLightDirectional
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

        mCameraFree.projection.setPerspective(
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