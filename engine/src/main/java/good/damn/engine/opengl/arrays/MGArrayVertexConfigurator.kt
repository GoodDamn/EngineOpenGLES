package good.damn.engine.opengl.arrays

import android.opengl.GLES30.GL_ARRAY_BUFFER
import android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER
import android.opengl.GLES30.GL_FLOAT
import android.opengl.GLES30.GL_STATIC_DRAW
import android.opengl.GLES30.glBindBuffer
import android.opengl.GLES30.glBindVertexArray
import android.opengl.GLES30.glBufferData
import android.opengl.GLES30.glEnableVertexAttribArray
import android.opengl.GLES30.glGenBuffers
import android.opengl.GLES30.glGenVertexArrays
import android.opengl.GLES30.glVertexAttribPointer
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

class MGArrayVertexConfigurator(
    val config: MGEnumArrayVertexConfiguration
) {

    val vertexArrayId: Int
        get() = mVertexArray[0]

    val indicesCount: Int
        get() = mIndicesSize

    private val mVertexArrayBuffer = intArrayOf(1)

    private val mVertexArray = intArrayOf(1)
    private val mIndicesArrayBuffer = intArrayOf(1)

    private var mIndicesSize = 0

    fun configure(
        vertices: FloatBuffer,
        indices: Buffer,
        pointerAttribute: MGPointerAttribute
    ) {
        mIndicesSize = indices.capacity()
        glGenVertexArrays(
            mVertexArray.size,
            mVertexArray,
            0
        )

        bindVertexArray()

        generateVertexBuffer(
            vertices
        )

        generateIndexBuffer(
            indices
        )

        pointerAttribute.bindPointers()
        unbind()
    }

    fun bindVertexArray() {
        glBindVertexArray(
            mVertexArray[0]
        )
    }

    fun bindVertexBuffer() {
        glBindBuffer(
            GL_ARRAY_BUFFER,
            mVertexArrayBuffer[0]
        )
    }

    fun unbind() {
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

    fun sendVertexBufferData(
        vertices: FloatBuffer
    ) {
        glBufferData(
            GL_ARRAY_BUFFER,
            vertices.capacity() * Float.SIZE_BYTES,
            vertices,
            GL_STATIC_DRAW
        )
    }

    private inline fun generateVertexBuffer(
        vertices: FloatBuffer
    ) {
        glGenBuffers(
            mVertexArrayBuffer.size,
            mVertexArrayBuffer,
            0
        )

        bindVertexBuffer()

        sendVertexBufferData(
            vertices
        )
    }

    private inline fun generateIndexBuffer(
        indices: Buffer
    ) {
        glGenBuffers(
            mIndicesArrayBuffer.size,
            mIndicesArrayBuffer,
            0
        )

        glBindBuffer(
            GL_ELEMENT_ARRAY_BUFFER,
            mIndicesArrayBuffer[0]
        )

        glBufferData(
            GL_ELEMENT_ARRAY_BUFFER,
            indices.capacity() * config.indicesSize,
            indices,
            GL_STATIC_DRAW
        )
    }
}