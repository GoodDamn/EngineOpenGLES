package good.damn.ia3d

import good.damn.ia3d.creators.A3DCreator
import good.damn.ia3d.creators.A3DCreatorMaterial
import good.damn.ia3d.models.A3DMAsset
import good.damn.ia3d.stream.A3DInputStream
import good.damn.ia3d.utils.A3DUtils
import java.io.DataInputStream
import java.io.IOException
import java.nio.ByteOrder
import kotlin.jvm.Throws

object A3DImport {

    private val SIGNATURE = byteArrayOf(
        0x41, 0x33, 0x44, 0 // A3D\0
    )

    private val VERSION_2 = 2.toShort()

    @Throws(
        IOException::class
    ) fun createFromStream(
        stream: A3DInputStream,
        buffer: ByteArray
    ): A3DMAsset? {
        stream.read(
            buffer,
            0,
            SIGNATURE.size
        )

        if (!(buffer[0] == SIGNATURE[0] &&
            buffer[1] == SIGNATURE[1] &&
            buffer[2] == SIGNATURE[2] &&
            buffer[3] == SIGNATURE[3]
        )) {
            return null
        }

        val version = stream.readLInt()

        if (version.toShort() != VERSION_2) {
            stream.close()
            return null
        }
        val rootBlock = A3DCreator.readRootBlock(
            stream,
            buffer
        )

        stream.close()
        return rootBlock
    }

}