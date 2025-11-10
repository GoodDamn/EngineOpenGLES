package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMMaterial
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorMaterial
: MIICreatorObject<MIMMaterial> {

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Byte>,
        buffer: ByteArray
    ) = MIMMaterial(
        stream.readInt(),
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        optionalMask.removeLastOrNull()?.run {
            MIUtilsIO.readObjectsArray(
                stream,
                MICreatorParamScalar.INSTANCE,
                optionalMask,
                buffer
            )
        },
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        MIUtilsIO.readObjectsArray(
            stream,
            MICreatorParamTexture.INSTANCE,
            optionalMask,
            buffer
        ),
        optionalMask.removeLastOrNull()?.run {
            MIUtilsIO.readObjectsArray(
                stream,
                MICreatorParamVector2.INSTANCE,
                optionalMask,
                buffer
            )
        },
        optionalMask.removeLastOrNull()?.run {
            MIUtilsIO.readObjectsArray(
                stream,
                MICreatorParamVector3.INSTANCE,
                optionalMask,
                buffer
            )
        },
        optionalMask.removeLastOrNull()?.run {
            MIUtilsIO.readObjectsArray(
                stream,
                MICreatorParamVector4.INSTANCE,
                optionalMask,
                buffer
            )
        }
    )

}