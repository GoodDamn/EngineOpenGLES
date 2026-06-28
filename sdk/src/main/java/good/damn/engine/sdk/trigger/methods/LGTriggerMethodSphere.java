package good.damn.engine.sdk.trigger.methods;

import androidx.annotation.NonNull;

import good.damn.engine.sdk.matrices.SDMatrixInvert;

public final class LGTriggerMethodSphere
extends LGTriggerMethodInvert {
    private static final float RADIUS = 1f;

    public LGTriggerMethodSphere(
        @NonNull SDMatrixInvert matrix
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
