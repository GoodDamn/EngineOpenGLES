package good.damn.common.matrices;

import android.opengl.Matrix;

import androidx.annotation.NonNull;

public final class MGMatrixNormal
extends MGMatrixInvert {

    public final float[] normalMatrix = new float[16];

    public MGMatrixNormal(
        @NonNull final float[] model
    ) {
        super(model);
        Matrix.setIdentityM(
            normalMatrix, 0
        );
    }

    /*@Override
    public final void draw(
        @NonNull final MGIShaderNormal shader
    ) {
        GLES30.glUniformMatrix4fv(
            shader.getUniformNormalMatrix(),
            1,
            false,
            normalMatrix,
            0
        );
    }*/

    public final void calculateNormalMatrix() {
        Matrix.transposeM(
            normalMatrix,
            0,
            modelInverted,
            0
        );
    }
}
