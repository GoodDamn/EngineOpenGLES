package good.damn.engine.sdk.matrices;

import android.opengl.Matrix;

public class SDMatrixModel {
    public final float[] model = new float[16];

    public SDMatrixModel() {
        Matrix.setIdentityM(
            model, 0
        );
    }
}
