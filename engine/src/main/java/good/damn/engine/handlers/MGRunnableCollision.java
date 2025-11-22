package good.damn.engine.handlers;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.camera.MGCamera;
import good.damn.engine.opengl.managers.MGManagerTriggerLight;
import good.damn.engine.opengl.managers.MGManagerTriggerMesh;
import good.damn.engine.opengl.matrices.MGMatrixTranslate;

public final class MGRunnableCollision
implements Runnable {

    @NonNull
    private final MGManagerTriggerMesh managerTriggerMesh;

    @NonNull
    private final MGManagerTriggerLight managerTriggerLight;

    @NonNull
    private final MGCamera mCamera;

    public MGRunnableCollision(
        @NonNull final MGManagerTriggerMesh managerTriggerMesh,
        @NonNull final MGManagerTriggerLight managerTriggerLight,
        @NonNull final MGCamera camera
    ) {
        this.managerTriggerMesh = managerTriggerMesh;
        this.managerTriggerLight = managerTriggerLight;
        mCamera = camera;
    }

    @Override
    public void run() {
        synchronized (
            mCamera
        ) {
            // 1. Camera point triggering needs to check only on self position changes
            // it doesn't need to check on each touch event
            // 2. For other entities who can trigger, check it inside infinite loop
            final MGMatrixTranslate matrix = mCamera.getModelMatrix();

            managerTriggerLight.loopTriggers(
                matrix.getX(),
                matrix.getY(),
                matrix.getZ()
            );

            managerTriggerMesh.loopTriggers(
                matrix.getX(),
                matrix.getY(),
                matrix.getZ()
            );
        }
    }
}
