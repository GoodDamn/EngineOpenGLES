package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMAtlas
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorAtlas
: MIICreatorObject<MIMAtlas> {
    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Boolean>,
        buffer: ByteArray
    ) = MIMAtlas(
        stream.readInt(),
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        stream.readInt(),
        stream.readInt(),
        stream.readInt()
    )
}