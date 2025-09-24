package good.damn.engine.opengl.shaders;

import static android.opengl.GLES20.glGetUniformLocation;

import android.opengl.GLES30;
import android.util.Log;

import good.damn.engine.opengl.drawers.MGIUniform;

public final class MGShaderLightPoint
implements MGIUniform {

    private int mUniformConstant;
    private int mUniformLinear;
    private int mUniformQuad;
    private int mUniformPosition;
    private int mUniformColor;

    private final String mId;

    public MGShaderLightPoint(
        final int id
    ) {
        mId = "lightPoints[" + id + "].";
    }

    @Override
    public final void setupUniforms(
        int program
    ) {
        mUniformConstant = glGetUniformLocation(
            program,
            mId+"constant"
        );

        mUniformLinear = glGetUniformLocation(
            program,
            mId+"linear"
        );

        mUniformQuad = glGetUniformLocation(
            program,
            mId+"quad"
        );

        mUniformPosition = glGetUniformLocation(
            program,
            mId+"position"
        );

        mUniformColor = glGetUniformLocation(
            program,
            mId+"color"
        );
        Log.d("MGShaderLightPoint", "setupUniforms: " + mId + " " + mUniformColor + " " + mUniformPosition + " ");
    }

    public final int getUniformConstant() {
        return mUniformConstant;
    }

    public final int getUniformLinear() {
        return mUniformLinear;
    }

    public final int getUniformQuad() {
        return mUniformQuad;
    }

    public final int getUniformPosition() {
        return mUniformPosition;
    }

    public final int getUniformColor() {
        return mUniformColor;
    }
}
