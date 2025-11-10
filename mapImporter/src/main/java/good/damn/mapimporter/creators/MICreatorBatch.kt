package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMBatch
import good.damn.mapimporter.misc.MIMVector3
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorBatch
: MIICreatorObject<MIMBatch> {

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Byte>,
        buffer: ByteArray
    ) = MIMBatch(
        stream.readInt(),
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        MIMVector3.read(stream),
        MIUtilsIO.readString(
            stream,
            buffer
        )
    )
}