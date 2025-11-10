package good.damn.mapimporter.utils;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import good.damn.mapimporter.creators.MIICreatorObject;
import kotlin.jvm.internal.markers.KMutableList;

public final class MIUtilsIO {

    public static Charset UTF_8 = StandardCharsets.UTF_8;

    public static int readArrayLength(
        @NotNull final DataInputStream stream
    ) throws IOException {
        byte arrFlags = stream.readByte();
        byte arrLenType = (byte) (arrFlags & 0b10000000);
        if (arrLenType == 0) {
            // Short array
            return arrFlags & 0b01111111;
        }

        // Long array
        arrLenType = (byte) (arrFlags & 0b01000000);
        byte len6 = (byte) (arrFlags & 0b00111111);
        if (arrLenType == 0) {
            return len6 << 8 + stream.readUnsignedByte();
        }

        return len6 << 16 + stream.readUnsignedShort();
    }

    public static String readString(
        @NotNull final DataInputStream stream,
        @NotNull final byte[] buffer
    ) throws IOException {
        final int arrLen = readArrayLength(
            stream
        );

        stream.read(
            buffer,
            0,
            arrLen
        );

        return new String(
            buffer,
            0,
            arrLen,
            UTF_8
        );
    }

    public static <T> List<T> readObjectsArray(
        @NotNull final DataInputStream stream,
        @NotNull final MIICreatorObject<T> creator,
        @NotNull final List<Byte> optionalMask,
        @NotNull final byte[] buffer
    ) throws IOException {
        final int arrLen = readArrayLength(
            stream
        );

        @NotNull final ArrayList<T> objs = new ArrayList<>(
            arrLen
        );

        for (
            int i = 0;
            i < arrLen;
            i++
        ) {
            objs.add(
                creator.create(
                    stream,
                    optionalMask,
                    buffer
                )
            );
        }

        return objs;
    }

}
