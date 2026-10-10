package good.damn.apigl;

import androidx.annotation.NonNull;

public final class GLApi {

    static {
        System.loadLibrary(
            "apiGL"
        );
    }

    private final long mApiRef;

    public GLApi() {
        mApiRef = create();
    }

    private native long create();

    public final int createProgram(
        @NonNull final String srcVertex,
        @NonNull final String srcFragment,
        final long binderAttribute
    ) {
        return createProgram(
            mApiRef,
            srcVertex,
            srcFragment,
            binderAttribute
        );
    }

    public final void drawMesh(
        int vertexArray,
        int mode,
        int typeIndices,
        int indicesCount
    ) {
        drawMesh(
            mApiRef,
            vertexArray,
            mode,
            typeIndices,
            indicesCount
        );
    }

    public final void setModelMatrix(
        int uniformLocation,
        float[] model
    ) {
        setModelMatrix(
            mApiRef,
            uniformLocation,
            model
        );
    }

    public final long createVertexAttribute(
        @NonNull final int[] attrs
    ) {
        return createVertexAttribute(
            mApiRef,
            attrs
        );
    }


    public final long createBinderAttribute(
        @NonNull final int[] attrs
    ) {
        return createBinderAttribute(
            mApiRef,
            attrs
        );
    }

    public final void useProgram(
        final int program
    ) {
        useProgram(
            mApiRef,
            program
        );
    }

    public final int getUniformLocation(
        final int program,
        @NonNull final String name
    ) {
        return getUniformLocation(
            mApiRef,
            program,
            name
        );
    }

    public final int createVertexArray(
        final long vertexAttribute,
        @NonNull final float[] vertices,
        @NonNull final int[] indices
    ) {
        return createVertexArray(
            mApiRef,
            vertexAttribute,
            vertices,
            indices
        );
    }

    public final long createTexture(
        final int fileDescriptor
    ) {
        return createTexture(
            mApiRef,
            fileDescriptor
        );
    }

    public final void setCullface() {
        setCullface(mApiRef);
    }

    public final void putTexture(
        long texture,
        int uniformTexture
    ) {
        putTexture(
            mApiRef,
            texture,
            uniformTexture
        );
    }








    private native int createProgram(
        long api,
        @NonNull final String srcVertex,
        @NonNull final String srcFragment,
        long binderAttribute
    );

    private native void drawMesh(
        long api,
        int vertexArray,
        int mode,
        int typeIndices,
        int indicesCount
    );


    private native void setModelMatrix(
        long api,
        int uniformLocation,
        float[] model
    );


    private native long createVertexAttribute(
        long api,
        int[] attrs
    );

    private native long createBinderAttribute(
        long api,
        int[] attrs
    );

    private native void useProgram(
        long api,
        int program
    );

    private native int getUniformLocation(
        long api,
        int program,
        @NonNull final String name
    );

    private native int createVertexArray(
        long api,
        long vertexAttribute,
        float[] vertices,
        int[] indices
    );

    private native void setCullface(
        long api
    );

    private native long createTexture(
        long api,
        int fileDescriptor
    );

    private native void putTexture(
        long api,
        long texture,
        int uniformTexture
    );

    public native void releaseBinderAttribute(
        long reference
    );

    public native void releaseVertexAttribute(
        long reference
    );
}
