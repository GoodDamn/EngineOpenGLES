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
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

open class MGArrayVertexConfigurator(
    val config: MGEnumArrayVertexConfiguration
) {

    companion object {
        const val STRIDE = 8 * 4

        const val MAX_VALUES_PER_VERTICES = 8

        private const val INDEX_POSITION = 0
        private const val INDEX_TEX_COORD = 1
        private const val INDEX_NORMAL = 2
    }

    val vertexArrayId: Int
        get() = mVertexArray[0]

    val indicesCount: Int
        get() = mIndicesSize

    protected val mVertexArrayBuffer = intArrayOf(1)

    private val mVertexArray = intArrayOf(1)
    private val mIndicesArrayBuffer = intArrayOf(1)

    private var mIndicesSize = 0

    open fun configure(
        vertices: FloatBuffer,
        indices: Buffer,
        stride: Int
    ) {
        mIndicesSize = indices.capacity()
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
            stride
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

    protected fun sendVertexBufferData(
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

        glBindBuffer(
            GL_ARRAY_BUFFER,
            mVertexArrayBuffer[0]
        )

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

    private inline fun enableAttrs(
        stride: Int
    ) {
        enableVertex(
            INDEX_POSITION,
            0,
            3,
            stride
        )
        if (stride <= 12) {
            return
        }

        enableVertex(
            INDEX_NORMAL,
            5 * 4,
            3,
            stride
        )

        enableVertex(
            INDEX_TEX_COORD,
            3 * 4,
            2,
            stride
        )
    }

    private inline fun enableVertex(
        attrib: Int,
        offset: Int,
        size: Int,
        stride: Int
    ) {
        glEnableVertexAttribArray(
            attrib
        )

        glVertexAttribPointer(
            attrib,
            size,
            GL_FLOAT,
            false,
            stride,
            offset
        )
    }
}