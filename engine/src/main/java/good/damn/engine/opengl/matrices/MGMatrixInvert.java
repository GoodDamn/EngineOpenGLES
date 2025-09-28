package good.damn.engine.opengl.matrices;

import android.opengl.Matrix;

import androidx.annotation.NonNull;

public class MGMatrixInvert {

    public final float[] modelInverted = new float[16];

    @NonNull
    private final float[] model;

    public MGMatrixInvert(
        @NonNull final float[] model
    ) {
        this.model = model;
        Matrix.setIdentityM(
            modelInverted, 0
        );
    }

    public final void calculateInvertModel() {
        Matrix.invertM(
            modelInverted,
            0,
            model,
            0
        );
    }
}
