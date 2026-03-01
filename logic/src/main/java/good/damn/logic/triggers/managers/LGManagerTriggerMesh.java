package good.damn.logic.triggers.managers;

import androidx.annotation.NonNull;

import good.damn.engine.sdk.matrices.SDMatrixScaleRotation;
import good.damn.logic.triggers.LGITrigger;

public final class LGManagerTriggerMesh
extends LGManagerTrigger<
    LGITrigger
> {
    @Override
    public synchronized void loopTriggers(
        float checkX,
        float checkY,
        float checkZ
    ) {
        @NonNull
        SDMatrixScaleRotation matrix;

        for (
            @NonNull
            final LGITrigger trigger : mTriggers
        ) {
            matrix = trigger.getModelMatrix();
            position4[0] = checkX - matrix.getX();
            position4[1] = checkY - matrix.getY();
            position4[2] = checkZ - matrix.getZ();

            trigger.trigger(
                position4
            );
        }
    }
}
