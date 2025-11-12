package good.damn.ia3d.creators

import good.damn.ia3d.misc.A3DMVector3
import good.damn.ia3d.models.A3DMMaterial
import good.damn.ia3d.stream.A3DInputStream
import good.damn.ia3d.utils.A3DUtils
import java.io.DataInputStream

object A3DCreatorMaterial {

    private val SIGNATURE = 4

    fun createFromStream(
        stream: A3DInputStream,
        buffer: ByteArray
    ): Array<A3DMMaterial>? {
        val sig = stream.readLInt()
        val sig2 = stream.readLInt()

        if (sig != SIGNATURE) {
            return null
        }

        val materialCount = stream.readLInt()
        return Array(
            materialCount
        ) {
            A3DMMaterial(
                A3DUtils.readNullTerminatedString(
                    stream,
                    buffer
                ),
                A3DMVector3.createFromStream(
                    stream
                ),
                A3DUtils.readNullTerminatedString(
                    stream,
                    buffer
                )
            )
        }
    }
}