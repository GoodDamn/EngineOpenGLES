package good.damn.engine.opengl.arrays

import android.opengl.GLES30.GL_ARRAY_BUFFER
import android.opengl.GLES30.glBindBuffer
import android.opengl.GLES30.glBindVertexArray
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import java.nio.FloatBuffer

class MGArrayVertexManager(
    type: MGEnumArrayVertexConfiguration
): MGArrayVertexConfigurator(
    type
) {

    companion object {
        const val INDEX_POSITION_X = 0
        const val INDEX_POSITION_Y = 1
        const val INDEX_POSITION_Z = 2
    }

    private var mBufferVertex: FloatBuffer? = null

    val sizeVertexArray: Int
        get() = mBufferVertex!!.capacity()

    val countVertices: Int
        get() = mBufferVertex!!.capacity() / MAX_VALUES_PER_VERTICES

    fun getVertexBufferData(
        iteration: Int,
        i: Int
    ) = mBufferVertex!![i + iteration * MAX_VALUES_PER_VERTICES]

    operator fun get(
        i: Int
    ) = mBufferVertex!![i]

    fun keepBufferVertices(
        vertices: FloatBuffer
    ) {
        mBufferVertex = vertices
    }

    fun unkeepBufferVertices() {
        mBufferVertex = null
    }

    fun sendVertexBufferData() {
        sendVertexBufferData(
            mBufferVertex!!
        )
    }

    fun bindVertexBuffer() {
        glBindVertexArray(
            vertexArrayId
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

    fun writeVertexBufferData(
        at: Int,
        data: Float
    ) {
        mBufferVertex!!.put(
            at,
            data
        )
    }

}