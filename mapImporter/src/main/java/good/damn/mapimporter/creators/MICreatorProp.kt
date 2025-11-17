package good.damn.mapimporter.creators

import good.damn.mapimporter.misc.MIMVector3
import good.damn.mapimporter.models.MIMProp
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream
import java.util.Queue

class MICreatorProp
: MIICreatorObject<MIMProp> {

    override fun create(
        stream: DataInputStream,
        optionalMask: Queue<Boolean>,
        buffer: ByteArray
    ) = MIMProp(
        if (optionalMask.remove()) MIUtilsIO.readString(
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
        if (optionalMask.remove())
            MIMVector3.read(stream)
        else null,
        if (optionalMask.remove())
            MIMVector3.read(stream)
        else null
    )
}