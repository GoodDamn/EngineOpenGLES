package good.damn.ia3d.creators

import good.damn.ia3d.models.A3DMMesh
import good.damn.ia3d.stream.A3DInputStream
import java.io.DataInputStream
import kotlin.experimental.and

object A3DCreatorMesh {

    private val SIGNATURE = 2.toShort()

    fun createFromStream(
        stream: A3DInputStream
    ): Array<A3DMMesh>? {
        val sign = stream.readLInt()
        stream.readLInt()
        if (sign.toShort() != SIGNATURE) {
            return null
        }

        val meshCount = stream.readLInt()

        return Array(
            meshCount
        ) {
            val vertexCount = stream.readLInt()
            val vertexBufferCount = stream.readLInt()

            val buffers = Array(vertexBufferCount) {
                A3DCreatorBufferVertex.createFromStream(
                    stream,
                    vertexCount
                )
            }

            val subMeshCount = stream.readLInt()

            A3DMMesh(
                buffers,
                Array(subMeshCount) {
                    A3DCreatorSubMesh.createFromStream(
                        stream
                    )
                }
            )
        }
    }
}