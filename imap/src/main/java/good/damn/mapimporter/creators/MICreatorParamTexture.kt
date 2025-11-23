package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMParamTexture
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream
import java.util.Queue

class MICreatorParamTexture
: MIICreatorObject<MIMParamTexture> {

    companion object {
        val INSTANCE = MICreatorParamTexture()
    }

    override fun create(
        stream: DataInputStream,
        optionalMask: Queue<Boolean>,
        buffer: ByteArray
    ) = MIMParamTexture(
        if (optionalMask.remove())
            MIUtilsIO.readString(stream, buffer)
        else null,
        MIUtilsIO.readString(stream,buffer),
        MIUtilsIO.readString(stream,buffer)
    )

}