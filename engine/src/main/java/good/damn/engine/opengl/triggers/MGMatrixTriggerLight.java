package good.damn.engine.opengl.triggers;

import androidx.annotation.NonNull;

import good.damn.engine.sdk.MGVector3;
import good.damn.engine.opengl.matrices.MGMatrixScale;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;

public final class MGMatrixTriggerLight {

    @NonNull
    public final MGMatrixTransformationInvert<
        MGMatrixScale
    > matrixTrigger;
    @NonNull
    private final MGVector3 mLightPosition;

    private float mRadius = 0f;

    public MGMatrixTriggerLight(
        @NonNull final MGMatrixTransformationInvert<
            MGMatrixScale
        > matrixTrigger
    ) {
        this.matrixTrigger = matrixTrigger;

        mLightPosition = new MGVector3(
            0f, 0f, 0f
        );

        setPosition(0f,0f,0f);
        invalidatePosition();

        setRadius(600f);
        invalidateRadius();
    }

    public final void invalidateRadius() {
        matrixTrigger.model.invalidateScale();
    }

    public final void invalidatePosition() {
        matrixTrigger.model.invalidatePosition();
    }

    public final void calculateInvertTrigger() {
        matrixTrigger.invert.calculateInvertModel();
    }

    public final void setPosition(
        final float x,
        final float y,
        final float z
    ) {
        matrixTrigger.model.setPosition(
            x, y, z
        );

        mLightPosition.setX(x);
        mLightPosition.setY(y);
        mLightPosition.setZ(z);
    }

    public final void setRadius(
        final float radius
    ) {
        matrixTrigger.model.setScale(
            radius,
            radius,
            radius
        );

        mRadius = radius;
    }

    @NonNull
    public final MGVector3 getPosition() {
        return mLightPosition;
    }

    public final float getRadius() {
        return mRadius;
    }
}
