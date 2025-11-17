package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMAtlas
import good.damn.mapimporter.models.MIMAtlasRect
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream
import java.util.Queue

class MICreatorAtlas
: MIICreatorObject<MIMAtlas> {
    override fun create(
        stream: DataInputStream,
        optionalMask: Queue<Boolean>,
        buffer: ByteArray
    ) = MIMAtlas(
        stream.readInt(),
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        stream.readInt(),
        MIUtilsIO.readObjectsArray(
            stream,
            MICreatorAtlasRect.INSTANCE,
            optionalMask,
            buffer
        ),
        stream.readInt()
    )
}