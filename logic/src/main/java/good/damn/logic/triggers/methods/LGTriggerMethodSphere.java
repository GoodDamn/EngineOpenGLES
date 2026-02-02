package good.damn.logic.triggers.methods;

import androidx.annotation.NonNull;

import good.damn.common.matrices.COMatrixInvert;

public final class LGTriggerMethodSphere
extends LGTriggerMethodInvert {
    private static final float RADIUS = 1f;

    public LGTriggerMethodSphere(
        @NonNull COMatrixInvert matrix
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
