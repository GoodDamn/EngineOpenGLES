package good.damn.engine.opengl.objects;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.nio.Buffer;
import java.nio.FloatBuffer;

import good.damn.engine.MGEngine;
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration;
import good.damn.engine.utils.MGUtilsBuffer;
import good.damn.engine.utils.MGUtilsFile;

public final class MGObject3d {

    @NonNull
    public final FloatBuffer vertices;

    @NonNull
    public final Buffer indices;

    @NonNull
    public final MGEnumArrayVertexConfiguration config;

    @Nullable
    public final String[] texturesDiffuseFileName;

    @Nullable
    public final String[] texturesMetallicFileName;

    @Nullable
    public final String[] texturesEmissiveFileName;

    public MGObject3d(
        @NonNull final FloatBuffer vertices,
        @NonNull final Buffer indices,
        @NonNull final MGEnumArrayVertexConfiguration config,
        @Nullable final String[] texturesDiffuseFileName,
        @Nullable final String[] texturesMetallicFileName,
        @Nullable final String[] texturesEmissiveFileName
    ) {
        this.vertices = vertices;
        this.indices = indices;
        this.config = config;

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
        this.vertices = MGUtilsBuffer.createFloat(
            vertices
        );

        @Nullable final Pair<
            MGEnumArrayVertexConfiguration,
            Buffer
        > pair = MGUtilsBuffer.INSTANCE.createBufferIndicesDynamic(
            indices,
            vertices.length / 8
        );

        this.indices = pair.second;
        config = pair.first;

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
        @NonNull final File filePub = MGUtilsFile.getPublicFile(
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
                MGEngine.Companion.getCharsetUTF8()
            )
        );
    }

    @Nullable
    private static native MGObject3d[] createFromPath(
        @NonNull byte[] path
    );
}
