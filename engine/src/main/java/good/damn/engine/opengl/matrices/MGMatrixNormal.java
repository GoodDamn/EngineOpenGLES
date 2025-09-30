package good.damn.engine.opengl.matrices;

import android.opengl.GLES30;
import android.opengl.Matrix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import good.damn.engine.opengl.drawers.MGIDrawer;
import good.damn.engine.opengl.shaders.MGIShaderNormal;

public final class MGMatrixNormal
extends MGMatrixInvert
implements MGIDrawer {

    public final float[] normalMatrix = new float[16];

    @Nullable
    public MGIShaderNormal shader;

    public MGMatrixNormal(
        @NonNull final MGIShaderNormal initShader,
        @NonNull final float[] model
    ) {
        super(model);
        shader = initShader;
        Matrix.setIdentityM(
            normalMatrix, 0
        );
    }

    @Override
    public final void draw() {
        if (shader == null) {
            return;
        }
        GLES30.glUniformMatrix4fv(
            shader.getUniformNormalMatrix(),
            1,
            false,
            normalMatrix,
            0
        );
    }

    public final void calculateNormalMatrix() {
        Matrix.transposeM(
            normalMatrix,
            0,
            modelInverted,
            0
        );
    }
}
