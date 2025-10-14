package good.damn.engine.opengl.triggers;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal;

public final class MGMatrixTriggerMesh {

    @NonNull
    public final MGMatrixTransformationInvert<
        MGMatrixScaleRotation
    > matrixTrigger;

    @NonNull
    public final MGMatrixTransformationNormal<
        MGMatrixScaleRotation
    > matrixMesh;

    @NonNull
    private final MGVector mTriggerScale;

    public MGMatrixTriggerMesh(
        @NonNull final MGMatrixTransformationInvert<
            MGMatrixScaleRotation
        > matrixTrigger,
        @NonNull final MGMatrixTransformationNormal<
            MGMatrixScaleRotation
        > matrixMesh,
        @NonNull final MGVector min,
        @NonNull final MGVector max
    ) {
        this.matrixTrigger = matrixTrigger;
        this.matrixMesh = matrixMesh;

        mTriggerScale = new MGVector(
            max.getX() - min.getX(),
            max.getY() - min.getY(),
            max.getZ() - min.getZ()
        );

        matrixTrigger.model.setScale(
            mTriggerScale.getX(),
            mTriggerScale.getY(),
            mTriggerScale.getZ()
        );
    }

    public final void invalidateScaleRotation() {
        matrixTrigger.model.invalidateScaleRotation();
        matrixMesh.model.invalidateScaleRotation();
    }

    public final void invalidatePosition() {
        matrixTrigger.model.invalidatePosition();
        matrixMesh.model.invalidatePosition();
    }

    public final void calculateInvertTrigger() {
        matrixTrigger.invert.calculateInvertModel();
    }

    public final void calculateNormalsMesh() {
        matrixMesh.normal.calculateInvertModel();
        matrixMesh.normal.calculateNormalMatrix();
    }

    public final void setScale(
        final float x,
        final float y,
        final float z
    ) {
        matrixTrigger.model.setScale(
            mTriggerScale.getX() * x,
            mTriggerScale.getY() * y,
            mTriggerScale.getZ() * z
        );

        matrixMesh.model.setScale(
            x, y, z
        );
    }

    public final void setPosition(
        final float x,
        final float y,
        final float z
    ) {
        matrixTrigger.model.setPosition(
            x, y, z
        );

        matrixMesh.model.setPosition(
            x, y, z
        );
    }

    public final void addRotation(
        final float x,
        final float y,
        final float z
    ) {
        matrixTrigger.model.mrx += x;
        matrixTrigger.model.mry += y;
        matrixTrigger.model.mrz += z;

        matrixMesh.model.mrx += x;
        matrixMesh.model.mry += y;
        matrixMesh.model.mrz += z;
    }

}
