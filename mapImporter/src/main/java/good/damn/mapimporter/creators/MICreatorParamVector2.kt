package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMParamVector2
import good.damn.mapimporter.misc.MIMVector2
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorParamVector2
: MIICreatorObject<MIMParamVector2> {

    companion object {
        val INSTANCE = MICreatorParamVector2()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Byte>,
        buffer: ByteArray
    ) = MIMParamVector2(
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        MIMVector2.read(stream)
    )
}