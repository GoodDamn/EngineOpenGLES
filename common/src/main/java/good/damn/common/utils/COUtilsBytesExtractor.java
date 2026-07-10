package good.damn.common.utils;

import androidx.annotation.NonNull;

public final class COUtilsBytesExtractor {

    public static final int readInt(
        @NonNull final byte[] buffer,
        final int index
    ) {
        return (buffer[index] & 0xff << 24) |
            (buffer[index+1] & 0xff << 16) |
            (buffer[index+2] & 0xff << 8) |
            (buffer[index+3] & 0xff);
    }
}
