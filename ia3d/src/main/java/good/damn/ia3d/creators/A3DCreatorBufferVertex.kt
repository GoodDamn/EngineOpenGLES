package good.damn.ia3d.creators

import good.damn.ia3d.enums.A3DEnumTypeBufferVertex
import good.damn.ia3d.models.A3DMBufferVertex
import good.damn.ia3d.stream.A3DInputStream

object A3DCreatorBufferVertex {

    private val VERTEX_SIZE = A3DEnumTypeBufferVertex.entries.toTypedArray()

    fun createFromStream(
        stream: A3DInputStream,
        vertexCount: Int
    ): A3DMBufferVertex? {
        val bufferType = stream.readLInt()
        var targetBuffer: A3DEnumTypeBufferVertex? = null
        for (it in VERTEX_SIZE) {
            if (it.type == bufferType) {
                targetBuffer = it
                break
            }
        }

        if (targetBuffer == null) {
            return null
        }

        val vertices = FloatArray(
            vertexCount * targetBuffer.vertexSize
        )

        for (i in 0 until vertexCount) {
            for (j in 0 until targetBuffer.vertexSize) {
                vertices[i] = stream.readLFloat()
            }
        }

        return A3DMBufferVertex(
            vertices,
            targetBuffer
        )
    }
}