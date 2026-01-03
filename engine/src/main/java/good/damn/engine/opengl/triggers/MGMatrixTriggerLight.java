package good.damn.engine.opengl.triggers;

import android.util.Log;

import androidx.annotation.NonNull;

import good.damn.engine.sdk.SDVector3;
import good.damn.engine.opengl.matrices.MGMatrixScale;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.sdk.models.SDMLightPointInterpolation;

public final class MGMatrixTriggerLight
implements MGIMatrixTrigger {

    @NonNull
    public final MGMatrixTransformationInvert<
        MGMatrixScale
    > matrixTrigger;
    @NonNull
    private final SDVector3 mLightPosition;

    public MGMatrixTriggerLight(
        @NonNull final MGMatrixTransformationInvert<
            MGMatrixScale
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

    public final void setRadius(
        float radius
    ) {
        setScale(
            radius,
            radius,
            radius
        );
    }

    public final void invalidateRadius() {
        invalidateScaleRotation();
    }

    public final void invalidatePosition() {
        matrixTrigger.model.invalidatePosition();
    }

    @Override
    public void invalidateScaleRotation() {
        matrixTrigger.model.invalidateScale();
    }

    public final void calculateInvertTrigger() {
        matrixTrigger.invert.calculateInvertModel();
    }

    @Override
    public void calculateNormals() {}

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

    @Override
    public void addPosition(
        float x,
        float y,
        float z
    ) {
        matrixTrigger.model.addPosition(
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
            x, y, z
        );
    }

    @Override
    public void setScale(
        float x,
        float y,
        float z
    ) {
        matrixTrigger.model.setScale(
            x, y, z
        );
    }

    @Override
    public void addRotation(
        float x,
        float y,
        float z
    ) {}

    @NonNull
    public final SDVector3 getPosition() {
        return mLightPosition;
    }
}
