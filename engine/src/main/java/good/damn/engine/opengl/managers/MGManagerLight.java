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
            @Nullable MGLight light = wrapper.light;

            if (light == null) {
                drawer.setRadius(0f);
                drawer.draw();
                continue;
            }

            drawer.setRadius(
                light.getRadius()
            );

            drawer.getColor().copy(
                light.getColor()
            );

            drawer.getPosition().copy(
                light.getPosition()
            );

            drawer.draw();
        }
    }

    public final void register(
        @NonNull final MGLight light
    ) {
        final int foundIndex = findFreeIndex();
        if (foundIndex == -1) {
            return;
        }

        mPullLights[
            foundIndex
        ].light = light;
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
        ].light = null;
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

    private static class MGLightWrapper {
        @Nullable
        MGLight light;

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
