package good.damn.ia3d.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class A3DUtils {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public static short readUnsignedShortL(
        byte[] buffer,
        int offset
    ) {
        return (short) (
            buffer[offset+1] & 0xff << 8 |
            buffer[offset] & 0xff
        );
    }

    public static String readNullTerminatedString(
        @NotNull final InputStream stream,
        byte[] buffer
    ) throws IOException {

        int readCount = 0;
        byte cha;
        while (true) {
            cha = (byte) stream.read();
            if (cha == 0) {
                break;
            }
            buffer[readCount] = cha;
            readCount++;
        }

        return new String(
            buffer,
            0,
            readCount,
            UTF_8
        );
    }

}
