package good.damn.logic.triggers.matrices;

import androidx.annotation.NonNull;

import good.damn.engine.sdk.SDVector3;
import good.damn.engine.sdk.matrices.SDMatrixScaleRotation;
import good.damn.engine.sdk.matrices.SDMatrixTransformationInvert;
import good.damn.engine.sdk.matrices.SDMatrixTransformationNormal;

public final class LGMatrixTriggerMesh {

    @NonNull
    public final SDMatrixTransformationInvert<
        SDMatrixScaleRotation
    > matrixTrigger;

    @NonNull
    public final SDMatrixTransformationNormal<
        SDMatrixScaleRotation
    > matrixMesh;

    @NonNull
    private final SDVector3 mTriggerScale;

    public LGMatrixTriggerMesh(
        @NonNull final SDMatrixTransformationInvert<
            SDMatrixScaleRotation
        > matrixTrigger,
        @NonNull final SDMatrixTransformationNormal<
            SDMatrixScaleRotation
        > matrixMesh,
        @NonNull final SDVector3 min,
        @NonNull final SDVector3 max
    ) {
        this.matrixTrigger = matrixTrigger;
        this.matrixMesh = matrixMesh;

        mTriggerScale = new SDVector3(
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

    public final void calculateNormals() {
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

    public void subtractScale(
        float x,
        float y,
        float z
    ) {
        matrixTrigger.model.subtractScale(
            mTriggerScale.getX() * x,
            mTriggerScale.getY() * y,
            mTriggerScale.getZ() * z
        );

        matrixMesh.model.subtractScale(
            x, y, z
        );
    }

    public void addScale(
        float x,
        float y,
        float z
    ) {
        matrixTrigger.model.addScale(
            mTriggerScale.getX() * x,
            mTriggerScale.getY() * y,
            mTriggerScale.getZ() * z
        );

        matrixMesh.model.addScale(
            x, y, z
        );
    }

    public final void addPosition(
        final float x,
        final float y,
        final float z
    ) {
        matrixTrigger.model.addPosition(
            x, y, z
        );

        matrixMesh.model.addPosition(
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
