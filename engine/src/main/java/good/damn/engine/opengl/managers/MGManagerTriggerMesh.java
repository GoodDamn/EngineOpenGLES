package good.damn.engine.opengl.managers;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.common.matrices.COMatrixScaleRotation;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;

public final class MGManagerTriggerMesh
extends MGManagerTrigger<
    MGDrawerTriggerStateable
> {
    public MGManagerTriggerMesh(
        @NonNull final MGDrawerVertexArray drawerTrigger
    ) {
        super(drawerTrigger);
    }

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
            final MGDrawerTriggerStateable trigger : mTriggers
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
