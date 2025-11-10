package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMAtlasRect
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream
import java.util.Queue

class MICreatorAtlasRect
: MIICreatorObject<MIMAtlasRect> {

    companion object {
        val INSTANCE = MICreatorAtlasRect()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: Queue<Boolean>,
        buffer: ByteArray
    ) = MIMAtlasRect(
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