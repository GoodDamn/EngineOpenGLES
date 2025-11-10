package good.damn.mapimporter.utils;

public final class MIUtilsBytes {

    public static int fromBytes3(
        byte[] inp
    ) {
        return (inp[0] & 0xff) << 16 |
            (inp[1] & 0xff) << 8 |
            (inp[2] & 0xff);
    }
}
