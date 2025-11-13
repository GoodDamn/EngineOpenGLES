package good.damn.ia3d.creators

import good.damn.ia3d.A3DImport
import good.damn.ia3d.enums.A3DEnumTypeBufferVertex
import good.damn.ia3d.models.A3DMBufferVertex
import good.damn.ia3d.models.A3DMSubMesh
import good.damn.ia3d.stream.A3DInputStream
import java.nio.ByteBuffer

object A3DCreatorSubMesh {

    fun createFromStream(
        stream: A3DInputStream
    ): A3DMSubMesh {
        val faceCount = stream.readLInt()
        val indexCount = faceCount * 3
        val indices = ByteBuffer.allocateDirect(
            indexCount * 2
        ).order(
            A3DImport.BYTE_ORDER
        ).asShortBuffer()

        for (i in 0 until indexCount) {
            indices.put(
                i,
                stream.readLUShort().toShort()
            )
        }

        val smoothGroups = IntArray(
            faceCount
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