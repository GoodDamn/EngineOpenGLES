package good.damn.mapimporter.utils;

public final class MIUtilsBytes {

    public static int fromBytes3(
        byte[] inp
    ) {
        return inp[0] << 16 |
            inp[1] << 8 |
            inp[2];
    }
}
