package good.damn.logic.triggers;

import androidx.annotation.NonNull;

import good.damn.common.matrices.COMatrixScale;
import good.damn.common.matrices.COMatrixTransformationInvert;
import good.damn.engine.sdk.SDVector3;

public final class LGMatrixTriggerLight {

    @NonNull
    public final COMatrixTransformationInvert<
            COMatrixScale
        > matrixTrigger;

    @NonNull
    private final SDVector3 mLightPosition;

    public LGMatrixTriggerLight(
        @NonNull final COMatrixTransformationInvert<
                    COMatrixScale
                > matrixTrigger
    ) {
        this.matrixTrigger = matrixTrigger;

        mLightPosition = new SDVector3(
            0f, 0f, 0f
        );

        setPosition(0f,0f,0f);
        invalidatePosition();
    }

    public final float getRadius() {
        return matrixTrigger.model.msx;
    }

    public final void subtractRadius(
        float radius
    ) {
        matrixTrigger.model.subtractScale(
            radius,
            radius,
            radius
        );
    }

    public final void setRadius(
        float radius
    ) {
        matrixTrigger.model.setScale(
            radius,
            radius,
            radius
        );
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

    @NonNull
    public final SDVector3 getPosition() {
        return mLightPosition;
    }
}
