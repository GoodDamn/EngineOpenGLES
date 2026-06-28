package good.damn.engine;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;

import good.damn.apigl.enums.GLEnumArrayVertexConfiguration;
import good.damn.common.utils.COUtilsFile;

public final class ASObject3d {

    @NonNull
    public final FloatBuffer vertices;

    @NonNull
    public final Buffer indices;

    @NonNull
    public final GLEnumArrayVertexConfiguration config;

    public ASObject3d(
        @NonNull final FloatBuffer vertices,
        @NonNull final Buffer indices,
        @NonNull final GLEnumArrayVertexConfiguration config
    ) {
        this.vertices = vertices;
        this.indices = indices;
        this.config = config;
    }

    public ASObject3d(
        @NonNull final float[] vertices,
        @NonNull final int[] indices
    ) {
        this.vertices = ASUtilsBuffer.createFloat(
            vertices
        );

        @NonNull final Pair<
            GLEnumArrayVertexConfiguration,
            Buffer
        > pair = ASUtilsBuffer.createBufferIndicesDynamic(
            indices,
            vertices.length / 8
        );

        this.indices = pair.second;
        config = pair.first;
    }

    static {
        System.loadLibrary(
            "assimpWrap"
        );
    }

    @Nullable
    public static ASObject3d[] createFromAssets(
        @NonNull final String localPath
    ) throws Exception {
        @NonNull final File filePub = COUtilsFile.getPublicFile(
            localPath
        );

        if (!filePub.exists()) {
            throw new Exception("No such file: " + filePub.getPath());
        }

        return createFromPath(
            filePub.getPath()
        );
    }

    @Nullable
    public static ASObject3d[] createFromPath(
        @NonNull String path
    ) {
        return createFromPath(
            path.getBytes(
                StandardCharsets.UTF_8
            )
        );
    }

    @Nullable
    private static native ASObject3d[] createFromPath(
        @NonNull byte[] path
    );
}
