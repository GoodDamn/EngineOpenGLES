package good.damn.engine.opengl.triggers.methods;

import android.opengl.Matrix;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.matrices.MGMatrixInvert;

public abstract class MGTriggerMethodInvert
implements MGITriggerMethod {

    @NonNull private final MGMatrixInvert matrixInvert;

    public MGTriggerMethodInvert(
        @NonNull final MGMatrixInvert matrix
    ) {
        matrixInvert = matrix;
    }

    // position4 float array should contains only 4 values
    // [x, y, z, 0.0f]
    @Override
    public final boolean canTrigger(
        @NonNull final float[] position4
    ) {
        Matrix.multiplyMV(
            position4,
            0,
            matrixInvert.modelInverted,
            0,
            position4,
            0
        );

        return canTriggerTransformed(
            position4[0],
            position4[1],
            position4[2]
        );
    }

    protected abstract boolean canTriggerTransformed(
        final float x,
        final float y,
        final float z
    );
}
