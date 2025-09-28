package good.damn.engine.opengl.matrices;

public final class MGMatrixTranslate
extends MGMatrixModel {

    private static final int INDEX_X = 3;
    private static final int INDEX_Y = 7;
    private static final int INDEX_Z = 11;

    private float mx = 0f;
    private float my = 0f;
    private float mz = 0f;

    public final void invalidatePosition() {
        model[INDEX_X] = mx;
        model[INDEX_Y] = my;
        model[INDEX_Z] = mz;
    }

    public final void setPosition(
        final float x,
        final float y,
        final float z
    ) {
        mx = x;
        my = y;
        mz = z;
    }

    public final void addPosition(
        final float x,
        final float y,
        final float z
    ) {
        mx += x;
        my += y;
        mz += z;
    }

    public final float getX() {
        return mx;
    }

    public final float getY() {
        return my;
    }

    public final float getZ() {
        return mz;
    }
}
