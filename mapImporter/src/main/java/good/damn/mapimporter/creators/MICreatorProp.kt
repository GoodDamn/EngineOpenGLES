package good.damn.mapimporter.creators

import good.damn.mapimporter.misc.MIMVector3
import good.damn.mapimporter.models.MIMProp
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorProp
: MIICreatorObject<MIMProp> {

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Boolean>,
        buffer: ByteArray
    ) = MIMProp(
        if (optionalMask.removeLast()) MIUtilsIO.readString(
            stream,
            buffer
        ) else null,
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
        if (optionalMask.removeLast())
            MIMVector3.read(stream)
        else null,
        if (optionalMask.removeLast())
            MIMVector3.read(stream)
        else null
    )
}