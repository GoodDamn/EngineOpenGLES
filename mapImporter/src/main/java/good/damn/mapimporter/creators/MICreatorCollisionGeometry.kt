package good.damn.mapimporter.creators

import good.damn.mapimporter.models.MIMCollisionBox
import good.damn.mapimporter.models.MIMCollisionGeometry
import good.damn.mapimporter.models.MIMCollisionPlane
import good.damn.mapimporter.models.MIMCollisionTriangle
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream
import java.util.Queue

class MICreatorCollisionGeometry
: MIICreatorObject<MIMCollisionGeometry> {

    override fun create(
        stream: DataInputStream,
        optionalMask: Queue<Boolean>,
        buffer: ByteArray
    ) = MIMCollisionGeometry(
        MIUtilsIO.readObjectsArray(
            stream,
            MICreatorCollisionBox.INSTANCE,
            optionalMask,
            buffer
        ),
        MIUtilsIO.readObjectsArray(
            stream,
            MICreatorCollisionPlane.INSTANCE,
            optionalMask,
            buffer
        ),
        MIUtilsIO.readObjectsArray(
            stream,
            MICreatorCollisionTriangle.INSTANCE,
            optionalMask,
            buffer
        )
    )

}