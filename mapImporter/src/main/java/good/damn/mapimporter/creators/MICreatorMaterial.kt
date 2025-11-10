package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMMaterial
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

class MICreatorMaterial
: MIICreatorObject<MIMMaterial> {

    override fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Boolean>,
        buffer: ByteArray
    ) = MIMMaterial(
        stream.readInt(),
        MIUtilsIO.readString(
            stream,
            buffer
        ),
        if (optionalMask.removeLast()) MIUtilsIO.readObjectsArray(
            stream,
            MICreatorParamScalar.INSTANCE,
            optionalMask,
            buffer
        ) else null,
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
        if (optionalMask.removeLast()) MIUtilsIO.readObjectsArray(
            stream,
            MICreatorParamVector2.INSTANCE,
            optionalMask,
            buffer
        ) else null,
        if (optionalMask.removeLast()) MIUtilsIO.readObjectsArray(
            stream,
            MICreatorParamVector3.INSTANCE,
            optionalMask,
            buffer
        ) else null,
        if (optionalMask.removeLast()) MIUtilsIO.readObjectsArray(
            stream,
            MICreatorParamVector4.INSTANCE,
            optionalMask,
            buffer
        ) else null
    )

}