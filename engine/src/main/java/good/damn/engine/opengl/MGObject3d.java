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

    @Nullable
    public final String[] texturesDiffuseFileName;

    @Nullable
    public final String[] texturesMetallicFileName;

    @Nullable
    public final String[] texturesEmissiveFileName;

    public MGObject3d(
        @NonNull final FloatBuffer vertices,
        @NonNull final int[] indices,
        @Nullable final String[] texturesDiffuseFileName,
        @Nullable final String[] texturesMetallicFileName,
        @Nullable final String[] texturesEmissiveFileName
    ) {
        this.vertices = vertices;
        this.indices = MGUtilsBuffer.Companion.createInt(
            indices
        );

        this.texturesDiffuseFileName = texturesDiffuseFileName;
        this.texturesMetallicFileName = texturesMetallicFileName;
        this.texturesEmissiveFileName = texturesEmissiveFileName;
    }

    public MGObject3d(
        @NonNull final float[] vertices,
        @NonNull final int[] indices,
        @Nullable final String[] texturesDiffuseFileName,
        @Nullable final String[] texturesMetallicFileName,
        @Nullable final String[] texturesEmissiveFileName
    ) {
        this.vertices = MGUtilsBuffer.Companion.createFloat(
            vertices
        );

        this.indices = MGUtilsBuffer.Companion.createInt(
            indices
        );

        this.texturesDiffuseFileName = texturesDiffuseFileName;
        this.texturesMetallicFileName = texturesMetallicFileName;
        this.texturesEmissiveFileName = texturesEmissiveFileName;
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

        return createFromPath(
            filePub.getPath()
        );
    }

    @Nullable
    public static MGObject3d[] createFromPath(
        @NonNull String path
    ) {
        return createFromPath(
            path.getBytes(
                PATH_CHARSET
            )
        );
    }

    @Nullable
    private static native MGObject3d[] createFromPath(
        @NonNull byte[] path
    );
}
