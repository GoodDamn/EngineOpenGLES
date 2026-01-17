package good.damn.logic.triggers.managers;

import androidx.annotation.NonNull;

import good.damn.common.matrices.COMatrixScaleRotation;
import good.damn.logic.triggers.entities.LGVolumeTrigger;

public final class LGManagerTriggerMesh
extends LGManagerTrigger<
    LGVolumeTrigger
> {
    @Override
    public synchronized void loopTriggers(
        float checkX,
        float checkY,
        float checkZ
    ) {
        @NonNull
        COMatrixScaleRotation matrix;

        for (
            @NonNull
            final LGVolumeTrigger trigger : mTriggers
        ) {
            matrix = trigger.getModelMatrix();
            position4[0] = checkX - matrix.getX();
            position4[1] = checkY - matrix.getY();
            position4[2] = checkZ - matrix.getZ();

            trigger.getStateManager().trigger(
                position4
            );
        }
    }
}
