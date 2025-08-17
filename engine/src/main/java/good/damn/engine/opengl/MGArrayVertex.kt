package good.damn.engine.opengl

import android.opengl.GLES30.*
import good.damn.engine.opengl.shaders.MGIShader
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

class MGArrayVertex {

    companion object {
        private const val STRIDE = 8 * 4
    }

    private val mVertexArray = intArrayOf(1)
    private val mVertexArrayBuffer = intArrayOf(1)

    private lateinit var mBufferVertex: FloatBuffer
    private lateinit var mBufferIndices: IntBuffer

    private var mIndicesSize = 0

    val sizeVertexArray: Int
        get() = mBufferVertex.capacity()

    operator fun get(
        i: Int
    ) = mBufferVertex[i]

    fun configure(
        shader: MGIShader,
        vertices: FloatBuffer,
        indices: IntBuffer
    ) {
        mBufferVertex = vertices
        mBufferIndices = indices
        mIndicesSize = indices.capacity()
        glGenVertexArrays(
            mVertexArray.size,
            mVertexArray,
            0
        )

        glBindVertexArray(
            mVertexArray[0]
        )

        generateVertexBuffer()

        generateIndexBuffer(
            indices
        )

        enableAttrs(
            shader
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

    fun getVertexBufferData(
        i: Int
    ) = mBufferVertex[i]

    fun bindVertexBuffer() {
        glBindVertexArray(
            mVertexArray[0]
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            mVertexArrayBuffer[0]
        )
    }

    fun unbindVertexBuffer() {
        glBindVertexArray(0)

        glBindBuffer(
            GL_ARRAY_BUFFER,
            0
        )
    }

    fun sendVertexBufferData() {
        glBufferData(
            GL_ARRAY_BUFFER,
            mBufferVertex.capacity() * 4,
            mBufferVertex,
            GL_STATIC_DRAW
        )
    }

    fun writeVertexBufferData(
        at: Int,
        data: Float
    ) {
        mBufferVertex.put(
            at,
            data
        )
        /*glBufferSubData(
            GL_ARRAY_BUFFER,
            at * 4,
            data.capacity() * 4,
            data
        )*/
    }

    fun draw(
        mode: Int = GL_TRIANGLES
    ) {
        glBindVertexArray(
            mVertexArray[0]
        )

        glDrawElements(
            mode,
            mIndicesSize,
            GL_UNSIGNED_INT,
            0
        )

        glBindVertexArray(0)
    }

    private inline fun generateVertexBuffer() {
        glGenBuffers(
            mVertexArrayBuffer.size,
            mVertexArrayBuffer,
            0
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            mVertexArrayBuffer[0]
        )

        sendVertexBufferData()
    }

    private inline fun generateIndexBuffer(
        indices: IntBuffer
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
            indices.capacity() * 4,
            indices,
            GL_STATIC_DRAW
        )
    }

    private inline fun enableAttrs(
        shader: MGIShader
    ) {
        enableVertex(
            shader.attribPosition,
            0,
            3
        )

        enableVertex(
            shader.attribTextureCoordinates,
            3 * 4,
            2
        )

        enableVertex(
            shader.attribNormal,
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
            STRIDE,
            offset
        )

    }

}