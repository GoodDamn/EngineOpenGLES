package good.damn.engine.opengl.triggers.methods;

import androidx.annotation.NonNull;

import good.damn.common.matrices.MGMatrixInvert;

public final class MGTriggerMethodSphere
extends MGTriggerMethodInvert {
    private static final float RADIUS = 1f;

    public MGTriggerMethodSphere(
        @NonNull MGMatrixInvert matrix
    ) { super(matrix); }

    @Override
    protected boolean canTriggerTransformed(
        float x,
        float y,
        float z
    ) {
        return Math.sqrt(
          x * x + y * y + z * z
        ) < RADIUS;
    }
}
