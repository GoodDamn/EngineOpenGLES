package good.damn.ia3d.stream;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import good.damn.ia3d.utils.A3DUtils;

public final class A3DInputStream
extends DataInputStream {

    private final byte[] mBuffer;

    public A3DInputStream(
        @NotNull final byte[] buffer,
        @NotNull InputStream inputStream
    ) {
        super(inputStream);
        mBuffer = buffer;
    }

    public A3DInputStream(
        @NotNull final InputStream stream
    ) {
        super(stream);
        mBuffer = new byte[16];
    }

    public int readLUShort() throws IOException {
        read(
            mBuffer,
            0, 2
        );

        return A3DUtils.readUnsignedShortL(
            mBuffer,
            0
        );
    }

    public int readLInt() throws IOException {
        read(
            mBuffer,
            0, 4
        );

        return (mBuffer[3] & 0xff) << 24 |
            (mBuffer[2] & 0xff) << 16 |
            (mBuffer[1] & 0xff) << 8 |
            (mBuffer[0] & 0xff);
    }

    public float readLFloat() throws IOException {
        return Float.intBitsToFloat(
            readLInt()
        );
    }
}
