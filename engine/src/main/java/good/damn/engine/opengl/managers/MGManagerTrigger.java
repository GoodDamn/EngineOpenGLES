package good.damn.engine.opengl.managers;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentLinkedQueue;

import good.damn.engine.opengl.drawers.MGIDrawer;
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;

public abstract class MGManagerTrigger<
    STATEABLE extends MGIDrawer
> implements MGIManagerTrigger {

    protected final float[] position4;

    protected final ConcurrentLinkedQueue<
        STATEABLE
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
        @NonNull final STATEABLE trigger
    ) {
        mTriggers.add(
            trigger
        );
    }

    public final void removeTrigger(
        @NonNull final STATEABLE trigger
    ) {
        mTriggers.remove(
            trigger
        );
    }

    @Override
    public final synchronized void draw() {
        for (
            @NonNull
            final STATEABLE trigger : mTriggers
        ) {
            trigger.draw();
        }
    }
}
