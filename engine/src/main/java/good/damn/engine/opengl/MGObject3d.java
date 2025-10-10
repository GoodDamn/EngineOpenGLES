package good.damn.engine.opengl;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import good.damn.engine.utils.MGUtilsBuffer;
import good.damn.engine.utils.MGUtilsFile;

public final class MGObject3d {

    private static final Charset PATH_CHARSET = StandardCharsets.UTF_8;

    @NonNull
    public final FloatBuffer vertices;

    @NonNull
    public final IntBuffer indices;

    public MGObject3d(
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

    @Nullable
    public static MGObject3d[] createFromAssets(
        @NonNull final String localPath
    ) throws Exception {
        @NonNull final File filePub = MGUtilsFile.Companion.getPublicFile(
            localPath
        );

        if (!filePub.exists()) {
            throw new Exception("No such file: " + filePub.getPath());
        }
        @NonNull final byte[] path = filePub.getPath().getBytes(
            PATH_CHARSET
        );

        Log.d("MGObject3d", "createFromAssets: SIZE: " + path.length + " CONTENT: " + Arrays.toString(path));

        return createFromStream(
            path
        );
    }

    private static native MGObject3d[] createFromStream(
        @NonNull byte[] path
    );
}
