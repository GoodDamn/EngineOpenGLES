package good.damn.engine.opengl.managers;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;

public final class MGManagerTriggerMesh
extends MGManagerTrigger<MGDrawerTriggerStateable> {

    @Override
    public synchronized void loopTriggers(
        float checkX,
        float checkY,
        float checkZ
    ) {
        @NonNull
        MGMatrixScaleRotation matrix;

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
