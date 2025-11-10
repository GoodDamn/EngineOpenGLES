package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMCollisionPlane
import good.damn.mapimporter.misc.MIMVector3
import java.io.DataInputStream

class MICreatorCollisionPlane
: MIICreatorObject<MIMCollisionPlane> {

    companion object {
        val INSTANCE = MICreatorCollisionPlane()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Byte>,
        buffer: ByteArray
    ) = MIMCollisionPlane(
        stream.readDouble(),
        MIMVector3.read(stream),
        MIMVector3.read(stream),
        stream.readDouble()
    )

}