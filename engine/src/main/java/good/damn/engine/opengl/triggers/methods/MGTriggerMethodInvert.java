package good.damn.engine.opengl.triggers.methods;

import android.opengl.Matrix;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.matrices.MGMatrixInvert;

public abstract class MGTriggerMethodInvert
implements MGITriggerMethod {

    protected final float[] mTransformedPosition = new float[4];
    private final float[] mTriggerPosition = new float[4];

    @NonNull private final MGMatrixInvert matrixInvert;

    public MGTriggerMethodInvert(
        @NonNull final MGMatrixInvert matrix
    ) {
        matrixInvert = matrix;
    }

    @Override
    public final boolean canTrigger(
        float x,
        float y,
        float z
    ) {
        mTriggerPosition[0] = x;
        mTriggerPosition[1] = y;
        mTriggerPosition[2] = z;
        mTriggerPosition[3] = 0.0f;

        Matrix.multiplyMV(
            mTransformedPosition,
            0,
            matrixInvert.modelInverted,
            0,
            mTriggerPosition,
            0
        );

        return canTriggerTransformed(
            mTransformedPosition[0],
            mTransformedPosition[1],
            mTransformedPosition[2]
        );
    }

    protected abstract boolean canTriggerTransformed(
        final float x,
        final float y,
        final float z
    );
}
