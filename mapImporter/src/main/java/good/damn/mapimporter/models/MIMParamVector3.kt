package good.damn.mapimporter.models

import good.damn.mapimporter.misc.MIMVector3
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

data class MIMParamVector3(
    val name: String,
    val value: MIMVector3
): MIIStreamRead<MIMParamVector3> {

    override fun read(
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