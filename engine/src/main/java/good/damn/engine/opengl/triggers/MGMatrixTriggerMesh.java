package good.damn.engine.opengl.triggers;

import androidx.annotation.NonNull;

import good.damn.engine.sdk.SDVector3;
import good.damn.common.matrices.COMatrixScaleRotation;
import good.damn.common.matrices.COMatrixTransformationInvert;
import good.damn.common.matrices.COMatrixTransformationNormal;

public final class MGMatrixTriggerMesh
implements MGIMatrixTrigger {

    @NonNull
    public final COMatrixTransformationInvert<
            COMatrixScaleRotation
        > matrixTrigger;

    @NonNull
    public final COMatrixTransformationNormal<
            COMatrixScaleRotation
        > matrixMesh;

    @NonNull
    private final SDVector3 mTriggerScale;

    public MGMatrixTriggerMesh(
        @NonNull final COMatrixTransformationInvert<
                    COMatrixScaleRotation
                > matrixTrigger,
        @NonNull final COMatrixTransformationNormal<
                    COMatrixScaleRotation
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

    @Override
    public final void invalidateScaleRotation() {
        matrixTrigger.model.invalidateScaleRotation();
        matrixMesh.model.invalidateScaleRotation();
    }

    @Override
    public final void invalidatePosition() {
        matrixTrigger.model.invalidatePosition();
        matrixMesh.model.invalidatePosition();
    }

    @Override
    public final void calculateInvertTrigger() {
        matrixTrigger.invert.calculateInvertModel();
    }

    @Override
    public final void calculateNormals() {
        matrixMesh.normal.calculateInvertModel();
        matrixMesh.normal.calculateNormalMatrix();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
