package good.damn.common.matrices;

import android.opengl.Matrix;

public class COMatrixModel {
    public final float[] model = new float[16];

    public COMatrixModel() {
        Matrix.setIdentityM(
            model, 0
        );
    }
}
