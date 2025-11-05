package good.damn.engine.opengl

import android.opengl.GLES30.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

class MGArrayVertex {

    companion object {
        const val STRIDE = 8 * 4
        private const val INDEX_POSITION = 0
        private const val INDEX_TEX_COORD = 1
        private const val INDEX_NORMAL = 2
        private const val INDEX_MODEL_INSTANCE = 3

        const val INDEX_POSITION_X = 0
        const val INDEX_POSITION_Y = 1
        const val INDEX_POSITION_Z = 2

        const val MAX_VALUES_PER_VERTICES = 8
    }

    private val mVertexArray = intArrayOf(1)
    private val mVertexArrayBuffer = intArrayOf(1)
    private val mIndicesArrayBuffer = intArrayOf(1)

    private lateinit var mBufferVertex: FloatBuffer
    private lateinit var mBufferIndices: IntBuffer

    private var mIndicesSize = 0

    val sizeVertexArray: Int
        get() = mBufferVertex.capacity()

    val countVertices: Int
        get() = mBufferVertex.capacity() / MAX_VALUES_PER_VERTICES

    operator fun get(
        i: Int
    ) = mBufferVertex[i]


    fun configure(
        vertices: FloatBuffer,
        indices: IntBuffer,
        stride: Int = STRIDE
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

    fun setupInstanceDrawing() {
        glBindVertexArray(
            mVertexArray[0]
        )

        val stride = 64
        val strideVector4 = 16
        val pos0 = INDEX_MODEL_INSTANCE
        val pos1 = INDEX_MODEL_INSTANCE + 1
        val pos2 = INDEX_MODEL_INSTANCE + 2
        val pos3 = INDEX_MODEL_INSTANCE + 3

        glEnableVertexAttribArray(pos0)
        glVertexAttribPointer(
            pos0,
            4,
            GL_FLOAT,
            false,
            stride,
            0
        )

        glEnableVertexAttribArray(pos1)
        glVertexAttribPointer(
            pos1,
            4,
            GL_FLOAT,
            false,
            stride,
            strideVector4
        )


        glEnableVertexAttribArray(
            pos2
        )
        glVertexAttribPointer(
            pos2,
            4,
            GL_FLOAT,
            false,
            stride,
            2*strideVector4
        )

        glEnableVertexAttribArray(pos3)
        glVertexAttribPointer(
            pos3,
            4,
            GL_FLOAT,
            false,
            stride,
            3*strideVector4
        )

        glVertexAttribDivisor(
            pos0, 1
        )

        glVertexAttribDivisor(
            pos1, 1
        )

        glVertexAttribDivisor(
            pos2, 1
        )

        glVertexAttribDivisor(
            pos3, 1
        )

        glBindVertexArray(0)
    }

    fun getVertexBufferData(
        iteration: Int,
        i: Int
    ) = mBufferVertex[i + iteration * MAX_VALUES_PER_VERTICES]

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

    fun drawInstanced(
        mode: Int = GL_TRIANGLES,
        count: Int
    ) {
        glBindVertexArray(
            mVertexArray[0]
        )

        glDrawElementsInstanced(
            mode,
            mIndicesSize,
            GL_UNSIGNED_INT,
            0,
            count
        )

        glBindVertexArray(0)
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
            indices.capacity() * 4,
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