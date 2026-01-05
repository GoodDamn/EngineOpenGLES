package good.damn.engine.opengl.matrices;

import android.opengl.Matrix;
import android.util.Log;

public final class MGMatrixScaleRotation
extends MGMatrixTranslate {

    public float msx = 1f;
    public float msy = 1f;
    public float msz = 1f;

    public float mrx = 0f;
    public float mry = 0f;
    public float mrz = 0f;

    public final void setScale(
        final float x,
        final float y,
        final float z
    ) {
        msx = x;
        msy = y;
        msz = z;
    }

    public final void addScale(
        float x,
        float y,
        float z
    ) {
        msx += x;
        msy += y;
        msz += z;
    }


    public final void subtractScale(
        float x,
        float y,
        float z
    ) {
        msx -= x;
        msy -= y;
        msz -= z;
    }

    public final void setRotation(
        final float x,
        final float y,
        final float z
    ) {
        mrx = x;
        mry = y;
        mrz = z;
    }

    public final void invalidateScaleRotation() {

        final float x = (float) (
            mrx * Math.PI / 180.0f
        );

        final float y = (float) (
            mry * Math.PI / 180.0f
        );

        final float z = (float) (
            mrz * Math.PI / 180.0f
        );

        float cx = (float) Math.cos(x);
        float sx = (float) Math.sin(x);

        float cy = (float) Math.cos(y);
        float sy = (float) Math.sin(y);

        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);


        float cxsy = cx * sy;
        float sxsy = sx * sy;
        // 3x3
        model[0] = (cy * cz) * msx;
        model[1] = (-cy * sz) * msx;
        model[2] = sy * msx;

        model[4] = (sxsy * cz + cx * sz) * msy;
        model[5] = (-sxsy * sz + cx * cz) * msy;
        model[6] = (-sx * cy) * msy;

        model[8] = (-cxsy * cz + sx * sz) * msz;
        model[9] = (cxsy * sz + sx * cz) * msz;
        model[10] = (cx * cy) * msz;
    }

}
