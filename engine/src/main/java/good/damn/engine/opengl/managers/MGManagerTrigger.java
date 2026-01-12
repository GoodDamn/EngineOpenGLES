package good.damn.engine.opengl.managers;

import android.opengl.GLES30;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentLinkedQueue;

import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.drawers.MGIDrawerShader;
import good.damn.engine.opengl.shaders.MGIShaderModel;

public abstract class MGManagerTrigger<
    STATEABLE extends MGIDrawerShader<MGIShaderModel>
> implements MGIManagerTrigger {

    protected final float[] position4;

    protected final ConcurrentLinkedQueue<
        STATEABLE
    > mTriggers = new ConcurrentLinkedQueue<>();

    @NonNull
    private final MGDrawerVertexArray mDrawerTrigger;

    public MGManagerTrigger(
        @NonNull final float[] position4,
        @NonNull final MGDrawerVertexArray drawerTrigger
    ) {
        if (position4.length != 4) {
            throw new IllegalArgumentException(
                "position4 float array should contains only 4 values"
            );
        }
        position4[3] = 0.0f;
        this.position4 = position4;
        mDrawerTrigger = drawerTrigger;
    }

    public MGManagerTrigger(
        @NonNull final MGDrawerVertexArray drawerTrigger
    ) {
        position4 = new float[4];
        position4[3] = 0.0f;
        mDrawerTrigger = drawerTrigger;
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
    public final synchronized void draw(
        @NonNull final MGIShaderModel shader
    ) {
        for (
            @NonNull
            final STATEABLE trigger : mTriggers
        ) {
            trigger.draw(
                shader
            );
            mDrawerTrigger.draw(
                GLES30.GL_LINES
            );
        }
    }
}
