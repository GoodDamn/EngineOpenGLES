package good.damn.ia3d.stream;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

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

        int a0 = mBuffer[0] & 0xff;
        int a1 = mBuffer[1] & 0xff;

        return a0 | a1 << 8;
    }

    public int readLInt() throws IOException {
        read(
            mBuffer,
            0, 4
        );
        int a0 = mBuffer[0] & 0xff;
        int a1 = mBuffer[1] & 0xff;
        int a2 = mBuffer[2] & 0xff;
        int a3 = mBuffer[3] & 0xff;

        return a0 | a1 << 8 | a2 << 16 | a3 << 24;
    }

    public float readLFloat() throws IOException {
        return Float.intBitsToFloat(
            readLInt()
        );
    }
}
