package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMCollisionTriangle
import good.damn.mapimporter.misc.MIMVector3
import java.io.DataInputStream

class MICreatorCollisionTriangle
: MIICreatorObject<MIMCollisionTriangle> {

    companion object {
        val INSTANCE = MICreatorCollisionTriangle()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Byte>,
        buffer: ByteArray
    ) = MIMCollisionTriangle(
        stream.readDouble(),
        MIMVector3.read(stream),
        MIMVector3.read(stream),
        MIMVector3.read(stream),
        MIMVector3.read(stream),
        MIMVector3.read(stream)
    )
}