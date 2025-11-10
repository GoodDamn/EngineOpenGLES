package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMParamTexture
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorParamTexture
: MIICreatorObject<MIMParamTexture> {

    companion object {
        val INSTANCE = MICreatorParamTexture()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Boolean>,
        buffer: ByteArray
    ) = MIMParamTexture(
        if (optionalMask.removeLast())
            MIUtilsIO.readString(stream, buffer)
        else null,
        MIUtilsIO.readString(stream,buffer),
        MIUtilsIO.readString(stream,buffer)
    )

}