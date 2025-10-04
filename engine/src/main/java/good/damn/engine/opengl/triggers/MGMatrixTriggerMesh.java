package good.damn.engine.opengl.triggers;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.matrices.MGMatrixScale;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal;
import good.damn.engine.opengl.matrices.MGMatrixTranslate;

public final class MGMatrixTriggerMesh {

    @NonNull
    public final MGMatrixTransformationInvert<
        MGMatrixScale
    > matrixTrigger;

    @NonNull
    public final MGMatrixTransformationNormal<
        MGMatrixScale
    > matrixMesh;

    @NonNull
    private final MGVector mTriggerScale;

    public MGMatrixTriggerMesh(
        @NonNull final MGMatrixTransformationInvert<
            MGMatrixScale
        > matrixTrigger,
        @NonNull final MGMatrixTransformationNormal<
            MGMatrixScale
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

    public final void invalidateScale() {
        matrixTrigger.model.invalidateScale();
        matrixMesh.model.invalidateScale();
    }

    public final void invalidatePosition() {
        matrixTrigger.model.invalidatePosition();
        matrixMesh.model.invalidatePosition();
    }

    public final void calculateInvertTrigger() {
        matrixTrigger.invert.calculateInvertModel();
    }

    public final void calculateNormalsMesh() {
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

}
