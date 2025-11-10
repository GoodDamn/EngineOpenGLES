package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMParamVector3
import good.damn.mapimporter.misc.MIMVector3
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorParamVector3
: MIICreatorObject<MIMParamVector3> {

    companion object {
        val INSTANCE = MICreatorParamVector3()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Byte>,
        buffer: ByteArray
    ) = MIMParamVector3(
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        MIMVector3.read(stream)
    )
}