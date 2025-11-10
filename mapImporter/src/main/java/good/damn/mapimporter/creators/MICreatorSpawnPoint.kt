package good.damn.mapimporter.creators

import good.damn.mapimporter.misc.MIMVector3
import good.damn.mapimporter.models.MIMSpawnPoint
import java.io.DataInputStream

class MICreatorSpawnPoint
: MIICreatorObject<MIMSpawnPoint> {

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Boolean>,
        buffer: ByteArray
    ) = MIMSpawnPoint(
        MIMVector3.read(stream),
        MIMVector3.read(stream),
        stream.readInt()
    )

}