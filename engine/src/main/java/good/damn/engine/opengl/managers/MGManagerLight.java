package good.damn.engine.opengl.managers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ConcurrentLinkedDeque;

import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.drawers.MGIDrawer;
import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint;
import good.damn.engine.opengl.entities.MGLight;
import good.damn.engine.opengl.shaders.MGIShaderLight;
import good.damn.engine.opengl.shaders.MGShaderDefault;
import good.damn.engine.opengl.shaders.MGShaderLightPoint;
import good.damn.engine.opengl.triggers.MGMatrixTriggerLight;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight;

public final class MGManagerLight
implements MGIDrawer {

    private final MGLightWrapper[] mPullLights;

    public MGManagerLight(
        @NonNull final MGShaderDefault shader
    ) {
        @NonNull
        final MGShaderLightPoint[] lightPoints = shader
            .getLightPoints();

        mPullLights = new MGLightWrapper[
            lightPoints.length
        ];

        for (int i = 0; i < mPullLights.length; i++) {
            mPullLights[i] = new MGLightWrapper(
                lightPoints[i]
            );
        }
    }

    @Override
    public final void draw() {
        for (
            MGLightWrapper wrapper : mPullLights
        ) {
            final MGDrawerLightPoint drawer = wrapper.drawer;

            @Nullable
            final MGDrawerTriggerStateableLight state = wrapper.state;
            if (state == null) {
                drawer.setActive(0);
                drawer.draw();
                continue;
            }

            @NonNull
            final MGLight light = state.getLight();

            @NonNull
            final MGMatrixTriggerLight matrix = state.getModelMatrix();

            drawer.setActive(1);

            drawer.setRadius(
                matrix.getRadius()
            );

            drawer.getColor().copy(
                light.getColor()
            );

            drawer.getPosition().copy(
                matrix.getPosition()
            );

            drawer.draw();
        }
    }

    public final void register(
        @NonNull final MGDrawerTriggerStateableLight state
    ) {
        final int foundIndex = findFreeIndex();
        if (foundIndex == -1) {
            return;
        }

        mPullLights[
            foundIndex
        ].state = state;
    }

    public final void unregister(
        @NonNull final MGDrawerTriggerStateableLight state
    ) {
        final int lockIndex = findLockIndex(
            state
        );

        if (lockIndex == -1) {
            return;
        }

        mPullLights[
            lockIndex
        ].state = null;
    }

    private final int findFreeIndex() {
        for (int i = 0; i < mPullLights.length; i++) {
            if (mPullLights[i].state == null) {
                return i;
            }
        }
        return -1;
    }

    private final int findLockIndex(
        @NonNull final MGDrawerTriggerStateableLight target
    ) {
        for (int i = 0; i < mPullLights.length; i++) {
            @Nullable
            final MGDrawerTriggerStateableLight foundState = mPullLights[i].state;
            if (foundState == null) {
                continue;
            }

            if (foundState.hashCode() == target.hashCode()) {
                return i;
            }
        }

        return -1;
    }

    private static class MGLightWrapper {
        @Nullable
        MGDrawerTriggerStateableLight state;

        @NonNull
        final MGDrawerLightPoint drawer;

        MGLightWrapper(
            @NonNull final MGShaderLightPoint shader
        ) {
            drawer = new MGDrawerLightPoint(
                shader
            );
        }
    }
}
