package good.damn.engine.opengl.models;

import android.util.Pair;

import androidx.annotation.NonNull;

import java.nio.IntBuffer;

import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.arrays.MGArrayVertexManager;
import good.damn.engine.opengl.entities.MGMaterial;

public final class MGMPoolMeshMutable {
    @NonNull public MGArrayVertexManager vertexArray;
    @NonNull public Pair<MGVector, MGVector> pointMinMax;
    @NonNull public MGMaterial material;
    @NonNull public MGVector pointMiddle;

    public final MGMPoolMesh toImmutable() {
        return new MGMPoolMesh(
            vertexArray,
            pointMinMax,
            material,
            pointMiddle
        );
    }
}
