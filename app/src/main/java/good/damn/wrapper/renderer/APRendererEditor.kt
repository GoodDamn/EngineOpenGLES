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
import good.damn.apigl.shaders.lightpass.GLShaderLightPassPointLight
import good.damn.common.COHandlerGl
import good.damn.common.COIRunnableBounds
import good.damn.common.camera.COCameraFree
import good.damn.common.camera.COCameraProjection
import good.damn.common.camera.COMCamera
import good.damn.common.matrices.COMatrixTranslate
import good.damn.common.vertex.COMArrayVertexManager
import good.damn.common.volume.COManagerFrustrum
import good.damn.engine.ASObject3d
import good.damn.engine.ASUtilsBuffer
import good.damn.engine2.camera.GLCameraFree
import good.damn.engine2.camera.GLCameraProjection
import good.damn.engine2.loaders.texture.MGLoaderTextureAsync
import good.damn.engine2.models.MGMDrawers
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.models.MGMManagers
import good.damn.engine2.models.MGMParameters
import good.damn.engine2.models.MGMGeometry
import good.damn.engine2.models.MGSky
import good.damn.engine2.drawmodes.MGDrawModesDefault
import good.damn.engine2.drawmodes.MGRunglCycleDrawerModes
import good.damn.engine2.pools.MGMPools
import good.damn.engine2.pools.MGPoolMaterials
import good.damn.engine2.pools.MGPoolMeshesStatic
import good.damn.engine2.pools.MGPoolTextures
import good.damn.engine2.shader.MGShaderCache
import good.damn.engine2.shader.MGShaderSource
import good.damn.engine2.utils.MGUtilsVertIndices
import good.damn.logic.process.LGManagerProcessTime
import good.damn.logic.triggers.managers.LGManagerTriggerMesh
import good.damn.script.SCLoaderScripts
import good.damn.engine2.files.MGFile
import good.damn.engine2.providers.MGMProviderGL
import good.damn.engine2.managers.MGStorageLightPass
import java.util.concurrent.ConcurrentLinkedQueue

class APRendererEditor(
    private val handlerGl: COHandlerGl
): COIRunnableBounds {


    private val mBuffer = ByteArray(
        1024 * 50
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
                handlerGl,
                mBufferUniformCamera
            ),
            GLCameraProjection(
                COCameraProjection(
                    this
                ),
                handlerGl,
                mBufferUniformCamera
            )
        )
    }

    private val mFramebufferG = GLFrameBufferG(
        GLFramebuffer()
    )

    private val mShaders = MGMInformatorShader(
        MGShaderSource(
            "opaque",
            mBuffer
        ),
        cacheGeometryPass = MGShaderCache(
            SparseArray(50),
            handlerGl,
            GLShaderCreatorGeomPassModel()
        ),
        cacheGeometryPassInstanced = MGShaderCache(
            SparseArray(50),
            handlerGl,
            GLShaderCreatorGeomPassInstanced()
        ),
        wireframe = GLShaderGeometryPassModel(
            GLShaderMaterial.empty
        ),
        lightPasses = MGStorageLightPass(
            mFramebufferG
        ),
        GLShaderLightPassPointLight.Builder()
            .attachAll()
            .build()
    )

    private val mParameters = MGMParameters(
        true,
        null
    )

    private val managerFrustrum = COManagerFrustrum(
        mCameraFree.projection,
        COMArrayVertexManager(
            verticesBox10Raw
        )
    )

    val providerModel = MGMProviderGL(
        geometry = MGMGeometry(
            ConcurrentLinkedQueue(),
            ConcurrentLinkedQueue(),
            ConcurrentLinkedQueue(),
            MGSky()
        ),
        pools = MGPoolTextures(
            MGLoaderTextureAsync(
                handlerGl
            )
        ).run {
            MGMPools(
                MGPoolMaterials(
                    this,
                    mShaders
                ),
                MGPoolMeshesStatic(
                    handlerGl
                ),
                this
            )
        },
        managers = MGMManagers(
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
                    GLDrawerVertexArray(
                        mVerticesSphere
                    )
                )
            ),
            managerFrustrum,
            LGManagerTriggerMesh()
        ),
        mShaders,
        mParameters,
        mCameraFree.camera,
        handlerGl,
        drawers = MGMDrawers(
            GLDrawerFramebufferG(
                mFramebufferG
            ),
            GLDrawerLightDirectional(),
            GLDrawerVolumes(
                GLDrawerVertexArray(
                    mVerticesBox10
                ),
                managerFrustrum
            ),
            GLDrawerVertexArray(
                mVerticesQuad
            )
        )
    )

    private val mDefaultDrawModes = MGDrawModesDefault(
        providerModel
    ).apply {
        switcherDrawMode.registerGlProvider(
            providerModel
        )
    }

    val switcherDrawMode: MGRunglCycleDrawerModes
        get() = mDefaultDrawModes.switcherDrawMode


    override fun run(
        width: Int,
        height: Int
    ) {
        mFramebufferG.generate(
            width, height
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

        val shaders = providerModel.shaders

        shaders.wireframe.setup(
            mBuffer,
            MGFile(
                "shaders/opaque/vert.glsl"
            ),
            MGFile(
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
                shaders.lightPasses.glSetupShaders(
                    mBuffer,
                    this
                )

                shaders.lightPassPointLight.setup(
                    mBuffer,
                    MGFile(
                        "shaders/lightPass/vert_pointLight.glsl"
                    ),
                    MGFile(
                        "shaders/opaque/defer/frag_defer_light_point.glsl"
                    ),
                    this
                )
            }

        providerModel.geometry.meshSky.configure(
            shaders,
            providerModel.pools.textures
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

        providerModel.managers.managerProcessTime.apply {
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
            providerModel.drawers.drawerLightDirectional
        )

        glDepthFunc(
            GL_LESS
        )

        glCullFace(
            GL_BACK
        )

        mCameraFree.projection.setPerspective(
            width,
            height
        )
    }
}