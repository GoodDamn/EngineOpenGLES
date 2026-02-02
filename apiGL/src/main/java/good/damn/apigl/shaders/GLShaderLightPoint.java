package good.damn.apigl.shaders;

import static android.opengl.GLES20.glGetUniformLocation;

import good.damn.apigl.drawers.GLIUniform;

public final class GLShaderLightPoint
implements GLIUniform {

    private int mUniformActive;
    private int mUniformConstant;
    private int mUniformLinear;
    private int mUniformPosition;
    private int mUniformColor;

    private int mUniformRadius;

    private final String mId;

    public GLShaderLightPoint() {
        mId = "lightPoint.";
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