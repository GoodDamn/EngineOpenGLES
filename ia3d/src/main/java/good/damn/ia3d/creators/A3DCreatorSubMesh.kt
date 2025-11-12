package good.damn.ia3d.creators

import good.damn.ia3d.enums.A3DEnumTypeBufferVertex
import good.damn.ia3d.models.A3DMBufferVertex
import good.damn.ia3d.models.A3DMSubMesh
import good.damn.ia3d.stream.A3DInputStream

object A3DCreatorSubMesh {

    fun createFromStream(
        stream: A3DInputStream
    ): A3DMSubMesh {
        val faceCount = stream.readLInt()
        val indexCount = faceCount * 3
        val indices = IntArray(indexCount)

        for (i in indices.indices) {
            indices[i] = stream.readLUShort()
        }

        val smoothGroups = IntArray(
            indexCount / 3
        )

        for (i in smoothGroups.indices) {
            smoothGroups[i] = stream.readLInt()
        }

        val materialId = stream.readLUShort()

        return A3DMSubMesh(
            indices,
            smoothGroups,
            materialId
        )
    }
}