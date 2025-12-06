package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.framebuffer.MGFramebuffer
import good.damn.engine.opengl.shaders.MGShaderPostProcess
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsVertIndices

class MGPostProcess {

    private val mArrayVertex = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mDrawerQuad = MGDrawerVertexArray(
        mArrayVertex
    )

    fun configure() {
        mArrayVertex.configure(
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
    }



    fun draw(
        width: Int,
        height: Int,
        shader: MGShaderPostProcess,
        textureId: Int
    ) {
        glViewport(
            0,
            0,
            width,
            height
        )

        glClearColor(
            0.0f,
            0.0f,
            0.0f,
            1.0f
        )
        glDisable(
            GL_DEPTH_TEST
        )
        glDisable(
            GL_CULL_FACE
        )
        glClear(
            GL_COLOR_BUFFER_BIT
        )
        // use post process shader
        shader.use()

        glActiveTexture(
            GL_TEXTURE0
        )

        glBindTexture(
            GL_TEXTURE_2D,
            textureId
        )

        glUniform1i(
            shader.uniformTexture,
            0
        )

        mDrawerQuad.draw(
            GL_TRIANGLES
        )

        glBindTexture(
            GL_TEXTURE_2D,
            0
        )
    }
}