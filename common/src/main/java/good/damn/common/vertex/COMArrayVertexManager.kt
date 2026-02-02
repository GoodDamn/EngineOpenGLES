package good.damn.common.vertex

import java.nio.FloatBuffer

data class COMArrayVertexManager(
    val vertices: FloatBuffer
) {
    companion object {
        const val INDEX_POSITION_X = 0
        const val INDEX_POSITION_Y = 1
        const val INDEX_POSITION_Z = 2

        const val MAX_VALUES_PER_VERTICES = 8
    }

    val sizeVertexArray: Int
        get() = vertices.capacity()

    val countVertices: Int
        get() = vertices.capacity() / MAX_VALUES_PER_VERTICES

    operator fun get(
        i: Int
    ) = vertices[i]

    operator fun set(
        i: Int,
        v: Float
    ) {
        vertices.put(
            i, v
        )
    }

    fun getVertexBufferData(
        iteration: Int,
        i: Int
    ) = vertices[
        i + iteration * MAX_VALUES_PER_VERTICES
    ]
}