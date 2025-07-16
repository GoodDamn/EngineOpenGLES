package good.damn.engine.opengl

import android.opengl.GLES30.*
import good.damn.engine.opengl.entities.MGMesh.Companion.mStride
import good.damn.engine.utils.MGUtilsBuffer

class MGArrayVertex {

    private val mVertexArray = intArrayOf(1)

    private var mIndicesSize = 0

    fun configure(
        program: Int,
        vertices: FloatArray,
        indices: ShortArray
    ) {
        mIndicesSize = indices.size
        glGenVertexArrays(
            mVertexArray.size,
            mVertexArray,
            0
        )

        glBindVertexArray(
            mVertexArray[0]
        )

        generateVertexBuffer(
            vertices
        )

        generateIndexBuffer(
            indices
        )

        enableAttrs(
            program
        )

        glBindVertexArray(
            0
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            0
        )

        glBindBuffer(
            GL_ELEMENT_ARRAY_BUFFER,
            0
        )
    }

    fun draw() {
        glBindVertexArray(
            mVertexArray[0]
        )

        glDrawElements(
            GL_TRIANGLES,
            mIndicesSize,
            GL_UNSIGNED_SHORT,
            0
        )

        glBindVertexArray(0)
    }

    private inline fun generateVertexBuffer(
        vertices: FloatArray
    ) {
        val vbo = intArrayOf(1)

        glGenBuffers(
            vbo.size,
            vbo,
            0
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            vbo[0]
        )

        glBufferData(
            GL_ARRAY_BUFFER,
            vertices.size * 4,
            MGUtilsBuffer.createFloat(
                vertices
            ),
            GL_STATIC_DRAW
        )
    }

    private inline fun generateIndexBuffer(
        indices: ShortArray
    ) {
        val ido = intArrayOf(1)

        glGenBuffers(
            ido.size,
            ido,
            0
        )

        glBindBuffer(
            GL_ELEMENT_ARRAY_BUFFER,
            ido[0]
        )

        glBufferData(
            GL_ELEMENT_ARRAY_BUFFER,
            indices.size * 2,
            MGUtilsBuffer.createShort(
                indices
            ),
            GL_STATIC_DRAW
        )
    }

    private inline fun enableAttrs(
        program: Int
    ) {
        enableVertex(
            glGetAttribLocation(
                program,
                "position"
            ),
            0,
            3
        )

        enableVertex(
            glGetAttribLocation(
                program,
                "texCoord"
            ),
            3 * 4,
            2
        )

        enableVertex(
            glGetAttribLocation(
                program,
                "normal"
            ),
            5 * 4,
            3
        )
    }

    private inline fun enableVertex(
        attrib: Int,
        offset: Int,
        size: Int
    ) {
        glEnableVertexAttribArray(
            attrib
        )

        glVertexAttribPointer(
            attrib,
            size,
            GL_FLOAT,
            false,
            mStride,
            offset
        )

    }

}