package good.damn.logic.triggers.managers;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class LGManagerTrigger<
    STATEABLE
> implements LGIManagerTrigger {

    protected final float[] position4;

    protected final ConcurrentLinkedQueue<
        STATEABLE
    > mTriggers = new ConcurrentLinkedQueue<>();

    public LGManagerTrigger(
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

    public LGManagerTrigger() {
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
}
