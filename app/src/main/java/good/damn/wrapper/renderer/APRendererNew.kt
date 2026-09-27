package good.damn.wrapper.renderer

import android.opengl.GLES20.GL_BACK
import android.opengl.GLES20.GL_LESS
import android.opengl.GLES20.GL_NO_ERROR
import android.opengl.GLES20.glCullFace
import android.opengl.GLES20.glDepthFunc
import android.opengl.GLES20.glGetError
import android.opengl.GLES30
import android.util.Log
import android.util.SparseArray
import good.damn.apigl.GLApi
import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.pointers.GLPointerAttribute
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
import good.damn.common.utils.COUtilsFile
import good.damn.common.vertex.COMArrayVertexManager
import good.damn.common.volume.COManagerFrustrum
import good.damn.engine.ASObject3d
import good.damn.engine.ASUtilsBuffer
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
import good.damn.engine2.files.MGFile
import good.damn.engine2.providers.MGMProviderGL
import good.damn.engine2.managers.MGStorageLightPass
import java.util.concurrent.ConcurrentLinkedQueue

class APRendererNew(
    private val glHandler: COHandlerGl,
    private val glApi: GLApi,
    private val glApiRef: Long
): COIRunnableBounds {


    override fun run(
        width: Int,
        height: Int
    ) {

        val program = glApi.createProgram(
            glApiRef,
            """
                uniform mat4 u_MVP;
                attribute vec4 position;
                attribute vec2 texCoord;
                
                void main() {
                    gl_Position = u_MVP * position;
                }
                
            """.trimIndent(),
            """
                precision mediump float;
                void main() {
                    gl_FragColor = vec4(0.8, 0.8, 0.8, 1.0);
                }
            """.trimIndent()
        )


        val descriptorAttributes = glApi.createVertexAttribute(
            glApiRef,
            intArrayOf(
                0, // position
                1 // texture coords
            )
        )

        ASObject3d.createFromFile(
            COUtilsFile.getPublicFile(
                "objs/sphere.fbx"
            )
        )?.get(0)?.apply {
            val descriptorVertexArray = glApi.createVertexArray(
                glApiRef,
                descriptorAttributes,
                rawVertices,
                rawIndices
            )

            glHandler.registerCycleTask(
                object: COIRunnableBounds {
                    override fun run(
                        width: Int,
                        height: Int
                    ) {
                        GLES30.glClearColor(
                            0.0f,
                            1.0f,
                            0.0f,
                            1.0f
                        )

                        GLES30.glUseProgram(
                            program
                        )

                        /*glApi.setModelMatrix(
                            glApiRef,
                            uniformLocation,
                        )*/

                        glApi.drawMesh(
                            glApiRef,
                            descriptorVertexArray,
                            GLES30.GL_TRIANGLES,
                            GLES30.GL_UNSIGNED_INT,
                            rawIndices!!.size
                        )
                    }

                }
            )
        }

        catchError(5)


        glDepthFunc(
            GL_LESS
        )

        glCullFace(
            GL_BACK
        )

    }

    private inline fun catchError(
        ind: Int
    ) {
        while (true) {
            val i = glGetError()
            if (i == GL_NO_ERROR) {
                break
            }
            Log.d("APRendererEditor", "run: ERROR$ind: ${i.toString(16)}")
        }
    }
}