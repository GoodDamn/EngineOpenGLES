package good.damn.engine.opengl.managers;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentLinkedDeque;

import good.damn.engine.opengl.entities.MGLight;

public final class MGManagerLight {

    private static final int NUM_LIGHTS = 32;

    private final MGLight[] mPullLights = new MGLight[
        NUM_LIGHTS
    ];

    public final void register(
        @NonNull final MGLight light
    ) {
        final int foundIndex = findFreeIndex();
        if (foundIndex == -1) {
            return;
        }

        mPullLights[
            foundIndex
        ] = light;
    }

    public final void unregister(
        @NonNull final MGLight light
    ) {
        final int lockIndex = findLockIndex(
            light
        );

        if (lockIndex == -1) {
            return;
        }

        mPullLights[
            lockIndex
        ] = null;
    }

    private final int findFreeIndex() {
        for (int i = 0; i < mPullLights.length; i++) {
            if (mPullLights[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private final int findLockIndex(
        @NonNull final MGLight target
    ) {
        for (int i = 0; i < mPullLights.length; i++) {
            if (mPullLights[i].hashCode() == target.hashCode()) {
                return i;
            }
        }

        return -1;
    }
}
