package good.damn.engine.opengl.managers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint;
import good.damn.engine.sdk.models.SDMLight;
import good.damn.engine.sdk.models.SDMLightPoint;
import good.damn.engine.opengl.shaders.MGShaderLightPoint;
import good.damn.engine.opengl.triggers.MGMatrixTriggerLight;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight;
import good.damn.engine.sdk.models.SDMLightPointInterpolation;

public final class MGManagerLight {

    private final ConcurrentLinkedQueue<
        MGDrawerLightPoint
    > mPullLights = new ConcurrentLinkedQueue<>();


    public final void draw(
        @NonNull final MGShaderLightPoint[] lightPoints
    ) {
        @NonNull
        final Iterator<
            MGDrawerLightPoint
        > iterator = mPullLights.iterator();

        for (
            @NonNull
            final MGShaderLightPoint lightPoint
            : lightPoints
        ) {
            if (iterator.hasNext()) {
                iterator.next().draw(
                    lightPoint
                );
                continue;
            }

            MGDrawerLightPoint.drawNull(
                lightPoint
            );
        }
    }

    public final void register(
        @NonNull final MGDrawerLightPoint drawer
    ) {
        mPullLights.add(
            drawer
        );
    }

    public final void unregister(
        @NonNull final MGDrawerLightPoint drawer
    ) {
        mPullLights.remove(
            drawer
        );
    }
}
