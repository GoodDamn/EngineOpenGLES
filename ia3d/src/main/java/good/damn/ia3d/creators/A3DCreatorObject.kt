package good.damn.ia3d.creators

import good.damn.ia3d.models.A3DMObject
import good.damn.ia3d.stream.A3DInputStream
import good.damn.ia3d.utils.A3DUtils
import java.io.DataInputStream

object A3DCreatorObject {

    private val SIGNATURE = 5

    fun createFromStream(
        stream: A3DInputStream,
        buffer: ByteArray
    ): Array<A3DMObject>? {
        val sig = stream.readLInt()
        val sig2 = stream.readLInt()
        if (sig != SIGNATURE) {
            return null
        }

        val count = stream.readLInt()

        return Array(
            count
        ) {
            A3DMObject(
                A3DUtils.readNullTerminatedString(
                    stream,
                    buffer
                ),
                stream.readLInt(),
                stream.readLInt()
            )
        }
    }
}