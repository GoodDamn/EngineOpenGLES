package good.damn.ia3d

import good.damn.ia3d.models.A3DMAsset
import java.io.DataInputStream
import java.io.IOException
import kotlin.jvm.Throws

object A3DImport {

    private val SIGNATURE = byteArrayOf(
        0x41, 0x33, 0x44, 0 // A3D\0
    )

    @Throws(
        IOException::class
    ) fun createFromStream(
        stream: DataInputStream,
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

        val version = stream.readUnsignedShort()

        stream.close()
        return A3DMAsset(
            version
        )
    }

}