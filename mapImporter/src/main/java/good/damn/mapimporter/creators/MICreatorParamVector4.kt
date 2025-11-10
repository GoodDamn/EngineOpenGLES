package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMParamVector4
import good.damn.mapimporter.misc.MIMVector4
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream
import java.util.Queue

class MICreatorParamVector4
: MIICreatorObject<MIMParamVector4> {

    companion object {
        val INSTANCE = MICreatorParamVector4()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: Queue<Boolean>,
        buffer: ByteArray
    ) = MIMParamVector4(
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        MIMVector4.read(stream)
    )
}