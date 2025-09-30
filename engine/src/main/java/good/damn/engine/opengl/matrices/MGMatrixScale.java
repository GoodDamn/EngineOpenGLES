package good.damn.engine.opengl.matrices;

import android.opengl.Matrix;

public final class MGMatrixScale
extends MGMatrixTranslate {

    private static final int INDEX_SX = 0;
    private static final int INDEX_SY = 5;
    private static final int INDEX_SZ = 10;

    public float msx = 1f;
    public float msy = 1f;
    public float msz = 1f;

    public final void setScale(
        final float x,
        final float y,
        final float z
    ) {
        msx = x;
        msy = y;
        msz = z;
    }

    public final void invalidateScale() {
        model[INDEX_SX] = msx;
        model[INDEX_SY] = msy;
        model[INDEX_SZ] = msz;
    }
}
