package good.damn.mapimporter.creators

import good.damn.mapimporter.misc.MIMVector3
import good.damn.mapimporter.models.MIMProp
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorProp
: MIICreatorObject<MIMProp> {

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Byte>,
        buffer: ByteArray
    ) = MIMProp(
        optionalMask.removeLastOrNull()?.run {
            MIUtilsIO.readString(
                stream,
                buffer
            )
        },
        stream.readInt(),
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        stream.readInt(),
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        MIMVector3.read(stream),
        optionalMask.removeLastOrNull()?.run {
            MIMVector3.read(stream)
        },
        optionalMask.removeLastOrNull()?.run {
            MIMVector3.read(stream)
        }
    )
}