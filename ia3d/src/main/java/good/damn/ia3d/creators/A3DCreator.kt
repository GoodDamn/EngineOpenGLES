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

        if (sig != SIGNATURE) {
            return null
        }

        A3DCreatorMaterial.createFromStream(
            stream
        )
    }
}