package good.damn.engine.opengl.managers;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint;
import good.damn.engine.opengl.shaders.MGShaderLightPoint;

public final class MGManagerLight {

    private final ConcurrentLinkedQueue<
        MGDrawerLightPoint
    > mLights = new ConcurrentLinkedQueue<>();


    public final void draw(
        @NonNull final MGShaderLightPoint[] lightPoints
    ) {
        @NonNull
        final Iterator<
            MGDrawerLightPoint
        > iterator = mLights.iterator();

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
        mLights.add(
            drawer
        );
    }

    public final void unregister(
        @NonNull final MGDrawerLightPoint drawer
    ) {
        mLights.remove(
            drawer
        );
    }
}
