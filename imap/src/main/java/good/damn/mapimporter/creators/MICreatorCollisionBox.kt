package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMCollisionBox
import good.damn.mapimporter.misc.MIMVector3
import java.io.DataInputStream
import java.util.Queue

class MICreatorCollisionBox
: MIICreatorObject<MIMCollisionBox> {

    companion object {
        val INSTANCE = MICreatorCollisionBox()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: Queue<Boolean>,
        buffer: ByteArray
    ) = MIMCollisionBox(
        MIMVector3.read(stream),
        MIMVector3.read(stream),
        MIMVector3.read(stream)
    )

}