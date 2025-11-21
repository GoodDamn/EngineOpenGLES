package good.damn.mapimporter

import good.damn.mapimporter.creators.MICreatorAtlas
import good.damn.mapimporter.creators.MICreatorBatch
import good.damn.mapimporter.creators.MICreatorCollisionGeometry
import good.damn.mapimporter.creators.MICreatorMaterial
import good.damn.mapimporter.creators.MICreatorProp
import good.damn.mapimporter.creators.MICreatorSpawnPoint
import good.damn.mapimporter.models.MIMMap
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream
import java.io.IOException

object MIImportMap {

    @Throws(IOException::class)
    fun createFromStream(
        inpStream: DataInputStream,
        buffer: ByteArray
    ): MIMMap {
        val stream = MIUtilsIO.decompressFile(
            inpStream
        )

        val optionalMasks = MIUtilsIO.readOptionalMask(
            stream
        )

        val atlases = if (
            optionalMasks.remove()
        ) MIUtilsIO.readObjectsArray(
            stream,
            MICreatorAtlas(),
            optionalMasks,
            buffer
        ) else ArrayList()

        val batches = if (
            optionalMasks.remove()
        ) MIUtilsIO.readObjectsArray(
            stream,
            MICreatorBatch(),
            optionalMasks,
            buffer
        ) else ArrayList()

        val geometry = MICreatorCollisionGeometry().create(
            stream,
            optionalMasks,
            buffer
        )

        val geometryOutsideGameZone = MICreatorCollisionGeometry().create(
            stream,
            optionalMasks,
            buffer
        )

        val materials = MIUtilsIO.readObjectsArray(
            stream,
            MICreatorMaterial(),
            optionalMasks,
            buffer
        )

        val spawnPoints = if (
            optionalMasks.remove()
        ) MIUtilsIO.readObjectsArray(
            stream,
            MICreatorSpawnPoint(),
            optionalMasks,
            buffer
        ) else ArrayList()

        val props = MIUtilsIO.readObjectsArray(
            stream,
            MICreatorProp(),
            optionalMasks,
            buffer
        )

        stream.close()

        return MIMMap(
            atlases,
            batches,
            geometry,
            geometryOutsideGameZone,
            materials,
            spawnPoints,
            props
        )
    }
}
