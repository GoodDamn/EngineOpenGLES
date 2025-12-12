package good.damn.engine.opengl.managers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint;
import good.damn.engine.opengl.entities.MGLight;
import good.damn.engine.opengl.shaders.MGShaderLightPoint;
import good.damn.engine.opengl.triggers.MGMatrixTriggerLight;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight;

public final class MGManagerLight {

    private final MGLightWrapper[] mPullLights;

    public MGManagerLight(
        final int countLights
    ) {
        mPullLights = new MGLightWrapper[
            countLights
        ];

        for (int i = 0; i < mPullLights.length; i++) {
            mPullLights[i] = new MGLightWrapper();
        }
    }

    public final void draw(
        @NonNull final MGShaderLightPoint[] lightPoints
    ) {
        for (
            short i = 0;
            i < lightPoints.length;
            i++
        ) {
            final MGLightWrapper wrapper = mPullLights[i];
            final MGDrawerLightPoint drawer = wrapper.drawer;
            final MGShaderLightPoint shaderLightPoint = lightPoints[i];

            @Nullable
            final MGDrawerTriggerStateableLight state = wrapper.state;
            if (state == null) {
                drawer.setActive(0);
                drawer.draw(
                    shaderLightPoint
                );
                continue;
            }

            @NonNull
            final MGLight light = state.getLight();

            @NonNull
            final MGMatrixTriggerLight matrix = state.getModelMatrix();

            drawer.setActive(1);

            drawer.setRadius(
                light.getRadiusClip()
            );

            drawer.getColor().copy(
                light.getColor()
            );

            drawer.getPosition().copy(
                matrix.getPosition()
            );

            drawer.setConstant(
                light.getConstant()
            );

            drawer.setLinear(
                light.getConstant()
            );

            drawer.setQuad(
                light.getQuad()
            );

            drawer.draw(
                shaderLightPoint
            );
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
        final MGDrawerLightPoint drawer = new MGDrawerLightPoint();
    }
}
