package good.damn.engine2.utils;

import androidx.annotation.ColorInt;

import good.damn.engine.sdk.SDVector3;

public final class MGUtilsVector3 {

    public static SDVector3 createFromColorInt(
        @ColorInt final int color
    ) {
        return new SDVector3(
            ((color >> 16) & 0xff) / 255f,
            ((color >> 8) & 0xff) / 255f,
            (color & 0xff) / 255f
        );
    }
}
