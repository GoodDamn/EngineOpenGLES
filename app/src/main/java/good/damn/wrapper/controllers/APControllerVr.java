package good.damn.wrapper.controllers;

import androidx.annotation.NonNull;

public final class APControllerVr {

    static {
        System.loadLibrary(
            "cardboard_jni"
        );
    }

    private long mRef = 0;

    private int mWidth;
    private int mHeight;

    public final int getWidth() {
        return mWidth;
    }

    public final int getHeight() {
        return mHeight;
    }

    public final void create(
        @NonNull final APIDrawer drawer
    ) {
        mRef = nativeOnCreate(
            0.060f,
            0.035f,
            0.042f,
            new float[]{40.0f, 40.0f, 40.0f, 40.0f},
            new float[]{0.441f, 0.156f},
            drawer
        );
    }

    public final void resume() {
        nativeOnResume(
            mRef
        );
    }

    public final void draw() {
        nativeOnDrawFrame(
            mRef
        );
    }

    public final void destroy() {
        nativeOnDestroy(
            mRef
        );
    }

    public final void pause() {
        nativeOnPause(
            mRef
        );
    }

    public final void setPosition(
        float x,
        float y,
        float z
    ) {
        setPosition(
            mRef,
            x, y, z
        );
    }

    public final void getPose(
        @NonNull final float[] matrixPoseOut,
        final int indexEye
    ) {
        getPose(
            mRef,
            matrixPoseOut,
            indexEye
        );
    }

    public final void setScreenParams(
        final int width,
        final int height,
        final float xdpi,
        final float ydpi
    ) {
        mWidth = width;
        mHeight = height;
        nativeSetScreenParams(
            mRef,
            width,
            height,
            xdpi,
            ydpi
        );
    }

    private native long nativeOnCreate(
        float interLensDistance,
        float trayToLensDistance,
        float screenToLensDistance,
        float[] fovHalfDegrees,
        float[] distortionCoeffs,
        @NonNull final APIDrawer drawer
    );

    private native void setPosition(
        long nativeApp,
        float x,
        float y,
        float z
    );

    private native void getPose(
        long nativeApp,
        float[] matrixPose,
        int indexEye
    );

    private native void nativeOnDestroy(long nativeApp);

    private native void nativeOnDrawFrame(long nativeApp);

    private native void nativeOnPause(long nativeApp);

    private native void nativeOnResume(long nativeApp);

    private native void nativeSetScreenParams(
        long nativeApp,
        int width,
        int height,
        float xdpi,
        float ydpi
    );

}
