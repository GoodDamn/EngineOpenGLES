package good.damn.ia3d.creators

import good.damn.ia3d.models.A3DMBufferVertex
import good.damn.ia3d.models.A3DMSubMesh
import good.damn.ia3d.stream.A3DInputStream
import good.damn.ia3d.utils.A3DUtilsBuffer

object A3DCreatorSubMesh {

    fun createFromStream(
        stream: A3DInputStream,
        buffers: Array<A3DMBufferVertex?>,
        vertexCount: Int,
    ): A3DMSubMesh {
        val faceCount = stream.readLInt()
        val indexCount = faceCount * 3
        val config = A3DUtilsBuffer.createBufferDynamic(
            indexCount,
            buffers,
            stream,
            vertexCount
        )

        val smoothGroups = IntArray(
            faceCount
        )

        for (i in smoothGroups.indices) {
            smoothGroups[i] = stream.readLInt()
        }

        val materialId = stream.readLUShort()

        config.buffer.position(0)

        return A3DMSubMesh(
            config,
            smoothGroups,
            materialId
        )
    }
}