package good.damn.mapimporter.utils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import good.damn.mapimporter.creators.MIICreatorObject;
import kotlin.jvm.internal.markers.KMutableList;

public final class MIUtilsIO {

    public static Charset UTF_8 = StandardCharsets.UTF_8;

    public static List<Boolean> readOptionalMask(
        @NotNull final DataInputStream stream
    ) throws IOException {
        final List<Boolean> masks = new LinkedList<>();
        final byte flags = stream.readByte();
        byte lenType = (byte) (flags & 0b10000000);

        if (lenType == 0) {
            final byte integratedOptionalBits = (byte) (flags << 3);

            for (
                byte i = 7;
                i > 2;
                i--
            ) {
                masks.add(
                    (integratedOptionalBits & ((int)Math.pow(2, i))) == 0
                );
            }

            final byte externalByteCount = (byte) (
                (flags & 0b01100000) >> 5
            );

            final byte[] externalBytes = new byte[
                externalByteCount
            ];

            stream.read(
                externalBytes
            );

            for (
                byte exByte : externalBytes
            ) {
                for (
                    byte i = 7;
                    i > -1;
                    i--
                ) {
                    masks.add(
                        (exByte & ((byte)Math.pow(2, i))) == 0
                    );
                }
            }

            return masks.reversed();
        }

        lenType = (byte) (flags & 0b01000000);
        int exByteCount;
        if (lenType == 0) {
            exByteCount = flags & 0b00111111;
        } else {
            int b = flags & 0b00111111;
            exByteCount = b << 16;
            exByteCount += stream.readShort();
        }

        final byte[] exBytes = new byte[
            exByteCount
        ];

        stream.read(
            exBytes
        );

        for (
            byte exByte : exBytes
        ) {
            for (
                byte i = 7;
                i > -1;
                i--
            ) {
                masks.add(
                    (exByte & ((byte)Math.pow(2, i))) == 0
                );
            }
        }

        return masks.reversed();
    }

    public static DataInputStream decompressFile(
        @NotNull final DataInputStream stream
    ) throws IOException {
        byte flags = stream.readByte();
        boolean hasCompression = (flags & 0b01000000) > 0;

        int fileLen;
        byte lenType = (byte) (flags & 0b10000000);

        if (lenType == 0) {
            fileLen = stream.readByte();
            fileLen += (flags & 0b00111111) << 8;
        } else {
            byte[] len3 = new byte[3];
            stream.read(len3);
            fileLen = MIUtilsBytes.fromBytes3(len3);
            fileLen += (flags & 0b00111111) << 24;
        }


        if (hasCompression) {
            return new DataInputStream(
                new GZIPInputStream(
                    stream
                )
            );
        }

        return stream;
    }

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
        int shiftLen;
        if (arrLenType == 0) {
            shiftLen = len6 << 8;
            return shiftLen + (stream.readByte() & 0xff);
        }
        shiftLen = len6 << 16;

        return shiftLen + stream.readUnsignedShort();
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
        @NotNull final List<Boolean> optionalMask,
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
