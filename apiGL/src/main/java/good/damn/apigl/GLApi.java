package good.damn.apigl;

import androidx.annotation.NonNull;

public final class GLApi {

    static {
        System.loadLibrary(
            "apiGL"
        );
    }

    public native long create();

    public native int createProgram(
        long api,
        @NonNull final String srcVertex,
        @NonNull final String srcFragment,
        long binderAttribute
    );

    public native void drawMesh(
        long api,
        int vertexArray,
        int mode,
        int typeIndices,
        int indicesCount
    );

    public native void setModelMatrix(
        long api,
        int uniformLocation,
        float[] model
    );

    public native long createVertexAttribute(
        long api,
        int[] attrs
    );

    public native long createBinderAttribute(
        long api,
        int[] attrs
    );

    public native void useProgram(
        long api,
        int program
    );

    public native int getUniformLocation(
        long api,
        int program,
        @NonNull final String name
    );

    public native int createVertexArray(
        long api,
        long vertexAttribute,
        float[] vertices,
        int[] indices
    );
}
