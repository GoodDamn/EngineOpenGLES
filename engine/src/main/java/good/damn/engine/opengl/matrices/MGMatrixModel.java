package good.damn.engine.opengl.matrices;

import android.opengl.Matrix;

public class MGMatrixModel {
    public final float[] model = new float[16];

    public MGMatrixModel() {
        Matrix.setIdentityM(
            model, 0
        );
    }
}
