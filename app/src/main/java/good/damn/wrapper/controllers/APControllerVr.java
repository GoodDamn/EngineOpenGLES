package good.damn.wrapper.controllers;

import androidx.annotation.NonNull;

public final class APControllerVr {

    static {
        System.loadLibrary(
            "cardboard_jni"
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

    private native void getPose(
        long nativeApp,
        float[] modelMatrix,
        int indexEye,
        float positionX,
        float positionY,
        float positionZ
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
