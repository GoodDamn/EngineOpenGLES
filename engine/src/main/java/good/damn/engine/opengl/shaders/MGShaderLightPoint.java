package good.damn.engine.opengl.shaders;

import static android.opengl.GLES20.glGetUniformLocation;

import android.opengl.GLES30;
import android.util.Log;

import good.damn.engine.opengl.drawers.MGIUniform;

public final class MGShaderLightPoint
implements MGIUniform {

    private int mUniformActive;
    private int mUniformConstant;
    private int mUniformLinear;
    private int mUniformQuad;
    private int mUniformPosition;
    private int mUniformColor;

    private int mUniformRadius;

    private final String mId;

    public MGShaderLightPoint(
        final int index
    ) {
        mId = "lightPoints[" + index + "].";
    }

    @Override
    public final void setupUniforms(
        int program
    ) {
        mUniformActive = glGetUniformLocation(
            program,
            mId+"isActive"
        );

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

        mUniformRadius = glGetUniformLocation(
            program,
            mId+"radius"
        );
    }

    public final int getUniformActive() {
        return mUniformActive;
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
    public final int getUniformRadius() {
        return mUniformRadius;
    }
}
