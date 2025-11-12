package good.damn.ia3d.creators

import good.damn.ia3d.models.A3DMAsset
import good.damn.ia3d.stream.A3DInputStream
import java.io.DataInputStream

object A3DCreator {

    private val SIGNATURE = 1.toShort()

    fun readRootBlock(
        stream: A3DInputStream,
        buffer: ByteArray
    ): A3DMAsset? {
        val sig = stream.readLUShort()

        if (sig.toShort() != SIGNATURE) {
            return null
        }

        val materials = A3DCreatorMaterial.createFromStream(
            stream,
            buffer
        )

        val meshes = A3DCreatorMesh.createFromStream(
            stream
        )

        val transforms = A3DCreatorTransform.createFromStream(
            stream
        )

        return A3DMAsset(

        )
    }
}