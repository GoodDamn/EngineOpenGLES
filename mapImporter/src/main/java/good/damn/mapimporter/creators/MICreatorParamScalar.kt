package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMParamScalar
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorParamScalar
: MIICreatorObject<MIMParamScalar> {

    companion object {
        val INSTANCE = MICreatorParamScalar()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Boolean>,
        buffer: ByteArray
    ) = MIMParamScalar(
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        stream.readFloat()
    )

}