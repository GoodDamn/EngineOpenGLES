package good.damn.engine.opengl;

import androidx.annotation.NonNull;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;

import good.damn.engine.utils.MGUtilsBuffer;
import good.damn.engine.utils.MGUtilsFile;

public final class MGObject3d {

    @NonNull
    public final FloatBuffer vertices;

    @NonNull
    public final IntBuffer indices;

    private MGObject3d(
        @NonNull final float[] vertices,
        @NonNull final int[] indices
    ) {
        this.vertices = MGUtilsBuffer.Companion.createFloat(
            vertices
        );

        this.indices = MGUtilsBuffer.Companion.createInt(
            indices
        );
    }

    static {
        System.loadLibrary(
            "engine"
        );
    }

    public static MGObject3d createFromAssets(
        @NonNull final String localPath
    ) throws Exception {
        @NonNull final File filePub = MGUtilsFile.Companion.getPublicFile(
            localPath
        );

        if (!filePub.exists()) {
            throw new Exception("No such file: " + filePub.getPath());
        }
        createFromStream(
            filePub.getPath()
        );
        return null;
    }

    private static native void createFromStream(
        @NonNull String path
    );
}
