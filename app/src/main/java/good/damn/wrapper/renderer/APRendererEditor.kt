package good.damn.wrapper.renderer

import android.opengl.GLES20.GL_BACK
import android.opengl.GLES20.GL_LESS
import android.opengl.GLES20.glCullFace
import android.opengl.GLES20.glDepthFunc
import android.opengl.GLES30.GL_UNIFORM_BUFFER
import android.util.SparseArray
import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.pointers.GLPointerAttribute
import good.damn.apigl.buffers.GLBuffer
import good.damn.apigl.buffers.GLBufferUniform
import good.damn.apigl.buffers.GLBufferUniformCamera
import good.damn.apigl.drawers.GLDrawerFramebufferG
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
import good.damn.common.camera.COCameraFree
import good.damn.common.camera.COCameraProjection
import good.damn.common.camera.COMCamera
import good.damn.common.matrices.COMatrixTranslate
import good.damn.common.utils.COUtilsFile
import good.damn.common.vertex.COMArrayVertexManager
import good.damn.common.volume.COManagerFrustrum
import good.damn.engine.ASObject3d
import good.damn.engine.ASUtilsBuffer
import good.damn.engine2.camera.GLCameraFree
import good.damn.engine2.camera.GLCameraProjection
import good.damn.engine2.loaders.texture.MGLoaderTextureAsync
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.models.MGMManagers
import good.damn.engine2.models.MGMParameters
import good.damn.engine2.opengl.MGMGeometry
import good.damn.engine2.opengl.MGMVolume
import good.damn.engine2.opengl.MGSky
import good.damn.engine2.opengl.MGSwitcherDrawMode
import good.damn.engine2.opengl.models.MGMLightPass
import good.damn.engine2.opengl.pools.MGMPools
import good.damn.engine2.opengl.pools.MGPoolMaterials
import good.damn.engine2.opengl.pools.MGPoolMeshesStatic
import good.damn.engine2.opengl.pools.MGPoolTextures
import good.damn.engine2.sensors.MGSensorGyroscope
import good.damn.engine2.shader.MGShaderCache
import good.damn.engine2.shader.MGShaderSource
import good.damn.engine2.utils.MGUtilsVertIndices
import good.damn.logic.process.LGManagerProcessTime
import good.damn.logic.triggers.managers.LGManagerTriggerMesh
import good.damn.script.SCLoaderScripts
import good.damn.script.SCScriptLightPlacement
import good.damn.wrapper.files.APFile
import good.damn.wrapper.hud.APHud
import java.util.concurrent.ConcurrentLinkedQueue

class APRendererEditor
: COIRunnableBounds {


    private val mBuffer = ByteArray(
        1024 * 50
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

    private val mVerticesBox10 = GLArrayVertexConfigurator(
        GLEnumArrayVertexConfiguration.BYTE
    )

    private val mVerticesQuad = GLArrayVertexConfigurator(
        GLEnumArrayVertexConfiguration.BYTE
    )

    private val verticesBox10Raw = ASUtilsBuffer.createFloat(
        MGUtilsVertIndices.createCubeVertices2(
            0.5f
        )
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

    private val mDrawerSphere = GLDrawerVertexArray(
        mVerticesSphere
    )

    private val mGeometry = MGMGeometry(
        ConcurrentLinkedQueue(),
        ConcurrentLinkedQueue(),
        MGSky(),
        GLDrawerVertexArray(
            mVerticesQuad
        ),
        mDrawerSphere
    )

    private val mParameters = MGMParameters(
        true,
        null
    )

    private val mVolume = MGMVolume(
        mDrawerVolumes,
        mParameters
    )

    private val mPools = MGPoolTextures(
        MGLoaderTextureAsync(
            mHandlerGl
        )
    ).run {
        MGMPools(
            MGPoolMaterials(
                this,
                mInformatorShader
            ),
            MGPoolMeshesStatic(
                mHandlerGl
            ),
            this
        )
    }

    private val managers = MGMManagers(
        LGManagerProcessTime(),
        GLDrawerLights(
            GLDrawerLightPass(
                arrayOf(
                    mFramebufferG.textureAttachmentPosition.texture,
                    mFramebufferG.textureAttachmentNormal.texture,
                    mFramebufferG.textureAttachmentColorSpec.texture,
                    mFramebufferG.textureAttachmentMisc.texture,
                    mFramebufferG.textureAttachmentDepth.texture,
                ),
                mDrawerSphere
            )
        ),
        managerFrustrum,
        LGManagerTriggerMesh()
    )


    private val mSwitcherDrawMode = MGSwitcherDrawMode(
        mFramebufferG,
        mInformatorShader,
        mGeometry,
        mVolume,
        mDrawerLightDirectional,
        GLDrawerFramebufferG(
            mFramebufferG
        ),
        managers.managerLight
    )

    private val mHud = APHud(
        mCameraFree.camera,
        requesterUserContent,
        mSwitcherDrawMode,
        mParameters,
        mPools,
        mInformatorShader,
        managers,
        mGeometry,
        mHandlerGl
    )

    val sensors = arrayListOf(
        MGSensorGyroscope(
            mCameraFree.camera
        )
    )

    init {
        val scriptLightPlacement = SCScriptLightPlacement(
            COUtilsFile.getPublicFile(
                "scripts"
            ),
            managers.managerProcessTime,
            managers.managerLight,
            managers.managerFrustrum
        )
        scriptLightPlacement.execute()


    }

    override fun run(
        width: Int,
        height: Int
    ) {
        mFramebufferG.generate(
            width, height
        )
        mHud.layout(
            width.toFloat(),
            height.toFloat()
        )

        mBufferUniformCamera.apply {
            buffer.generate()
            GLBufferUniform.setupBindingPoint(
                0,
                buffer,
                sizeBytes
            )
        }

        mVerticesQuad.configure(
            ASUtilsBuffer.createFloat(
                MGUtilsVertIndices.createQuadVertices()
            ),
            ASUtilsBuffer.createByte(
                MGUtilsVertIndices.createQuadIndices()
            ),
            GLPointerAttribute.Builder()
                .pointPosition2()
                .pointTextureCoordinates()
                .build()
        )

        mInformatorShader.wireframe.setup(
            mBuffer,
            APFile(
                "shaders/opaque/vert.glsl"
            ),
            APFile(
                "shaders/wireframe/frag_defer.glsl"
            ),
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
                        APFile(
                            it.vertPath
                        ),
                        APFile(
                            it.fragPath
                        ),
                        this
                    )
                }

                mInformatorShader.lightPassPointLight.setup(
                    mBuffer,
                    APFile(
                        "shaders/lightPass/vert_pointLight.glsl"
                    ),
                    APFile(
                        "shaders/opaque/defer/frag_defer_light_point.glsl"
                    ),
                    this
                )
            }

        mGeometry.meshSky.configure(
            mInformatorShader,
            mPools.textures
        )

        val pointPosition = GLPointerAttribute.Builder()
            .pointPosition()
            .build()

        ASObject3d.createFromAssets(
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

        val indicesBox = ASUtilsBuffer.createByte(
            MGUtilsVertIndices.createCubeIndices2()
        )


        mVerticesBox10.configure(
            verticesBox10Raw,
            indicesBox,
            pointPosition
        )

        managers.managerProcessTime.apply {
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

        mCameraFree.projection.setPerspective(
            width / 2,
            height
        )
    }
}