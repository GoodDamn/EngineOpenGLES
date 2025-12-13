package good.damn.engine.opengl.models;

import android.util.Pair;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.shaders.MGShaderOpaqueSingle;
import good.damn.engine.sdk.SDVector3;
import good.damn.engine.opengl.arrays.MGArrayVertexManager;
import good.damn.engine.opengl.entities.MGMaterial;

public final class MGMPoolMeshMutable {
    @NonNull public MGArrayVertexManager vertexArray;
    @NonNull public Pair<SDVector3, SDVector3> pointMinMax;
    @NonNull public MGMaterial material;
    @NonNull public SDVector3 pointMiddle;

    @NonNull public MGShaderOpaqueSingle shaderOpaque;

    @NonNull
    public final MGMPoolMesh toImmutable() {
        return new MGMPoolMesh(
            vertexArray,
            pointMinMax,
            material,
            pointMiddle,
            shaderOpaque
        );
    }
}
