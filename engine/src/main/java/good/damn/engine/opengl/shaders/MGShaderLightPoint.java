package good.damn.engine.opengl.shaders;

import static android.opengl.GLES20.glGetUniformLocation;

import android.opengl.GLES30;

import good.damn.engine.opengl.drawers.MGIUniform;

public final class MGShaderLightPoint
implements MGIUniform {

    private int mUniformConstant;
    private int mUniformLinear;
    private int mUniformQuad;
    private int mUniformPosition;
    private int mUniformColor;

    @Override
    public final void setupUniforms(
        int program
    ) {
        mUniformConstant = glGetUniformLocation(
            program,
            "lightPoint.constant"
        );

        mUniformLinear = glGetUniformLocation(
            program,
            "lightPoint.linear"
        );

        mUniformQuad = glGetUniformLocation(
            program,
            "lightPoint.quad"
        );

        mUniformPosition = glGetUniformLocation(
            program,
            "lightPoint.position"
        );

        mUniformColor = glGetUniformLocation(
            program,
            "lightPoint.color"
        );
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
