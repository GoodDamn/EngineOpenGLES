package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMParamVector4
import good.damn.mapimporter.misc.MIMVector4
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorParamVector4
: MIICreatorObject<MIMParamVector4> {

    companion object {
        val INSTANCE = MICreatorParamVector4()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Byte>,
        buffer: ByteArray
    ) = MIMParamVector4(
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        MIMVector4.read(stream)
    )
}