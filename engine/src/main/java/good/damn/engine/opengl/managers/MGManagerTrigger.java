package good.damn.engine.opengl.managers;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentLinkedQueue;

import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;

public final class MGManagerTrigger {

    private final float[] position4;

    private final ConcurrentLinkedQueue<
        MGDrawerTriggerStateable
    > mTriggers = new ConcurrentLinkedQueue<>();

    public MGManagerTrigger(
        @NonNull final float[] position4
    ) {
        if (position4.length != 4) {
            throw new IllegalArgumentException(
                "position4 float array should contains only 4 values"
            );
        }
        position4[3] = 0.0f;
        this.position4 = position4;
    }

    public MGManagerTrigger() {
        position4 = new float[4];
        position4[3] = 0.0f;
    }

    public final void addTrigger(
        @NonNull final MGDrawerTriggerStateable trigger
    ) {
        mTriggers.add(
            trigger
        );
    }

    public final void removeTrigger(
        @NonNull final MGDrawerTriggerStateable trigger
    ) {
        mTriggers.remove(
            trigger
        );
    }

    public final synchronized void draw() {
        for (
            @NonNull
            final MGDrawerTriggerStateable trigger : mTriggers
        ) {
            trigger.draw();
        }
    }

    public final synchronized void loopTriggers(
        final float checkX,
        final float checkY,
        final float checkZ
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
