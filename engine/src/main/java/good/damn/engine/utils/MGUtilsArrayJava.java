package good.damn.engine.utils;

import java.lang.reflect.Array;

public final class MGUtilsArrayJava {

    public static <T> T createArray(
        int size
    ) {
        return (T) new Object[size];
    }
}
