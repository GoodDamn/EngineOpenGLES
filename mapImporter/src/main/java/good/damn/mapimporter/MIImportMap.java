package good.damn.mapimporter;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import good.damn.mapimporter.creators.MICreatorAtlas;
import good.damn.mapimporter.creators.MICreatorBatch;
import good.damn.mapimporter.creators.MICreatorCollisionGeometry;
import good.damn.mapimporter.creators.MICreatorMaterial;
import good.damn.mapimporter.creators.MICreatorProp;
import good.damn.mapimporter.creators.MICreatorSpawnPoint;
import good.damn.mapimporter.models.MIMAtlas;
import good.damn.mapimporter.models.MIMBatch;
import good.damn.mapimporter.models.MIMCollisionGeometry;
import good.damn.mapimporter.models.MIMMap;
import good.damn.mapimporter.models.MIMMaterial;
import good.damn.mapimporter.models.MIMProp;
import good.damn.mapimporter.models.MIMSpawnPoint;
import good.damn.mapimporter.utils.MIUtilsIO;

public final class MIImportMap {

    public static MIMMap createFromStream(
        @NotNull final DataInputStream inpStream,
        @NotNull final byte[] buffer
    ) throws IOException {

        final DataInputStream stream = MIUtilsIO.decompressFile(
            inpStream
        );

        final List<Boolean> optionalMasks = MIUtilsIO.readOptionalMask(
            stream
        );

        final List<MIMAtlas> atlases = optionalMasks.removeLast()
            ? new ArrayList<>()
        : MIUtilsIO.readObjectsArray(
            stream,
            new MICreatorAtlas(),
            optionalMasks,
            buffer
        );

        final List<MIMBatch> batches = optionalMasks.removeLast()
            ? new ArrayList<>()
        : MIUtilsIO.readObjectsArray(
            stream,
            new MICreatorBatch(),
            optionalMasks,
            buffer
        );

        final MIMCollisionGeometry geometry = new MICreatorCollisionGeometry().create(
            stream,
            optionalMasks,
            buffer
        );

        final MIMCollisionGeometry geometryOutsideGameZone = new MICreatorCollisionGeometry().create(
            stream,
            optionalMasks,
            buffer
        );

        final List<MIMMaterial> materials = MIUtilsIO.readObjectsArray(
            stream,
            new MICreatorMaterial(),
            optionalMasks,
            buffer
        );

        final List<MIMSpawnPoint> spawnPoints = optionalMasks.removeLast()
            ? new ArrayList<>()
        : MIUtilsIO.readObjectsArray(
            stream,
            new MICreatorSpawnPoint(),
            optionalMasks,
            buffer
        );

        final List<MIMProp> props = MIUtilsIO.readObjectsArray(
            stream,
            new MICreatorProp(),
            optionalMasks,
            buffer
        );

        return new MIMMap(
            atlases,
            batches,
            geometry,
            geometryOutsideGameZone,
            materials,
            spawnPoints,
            props
        );
    }

}
