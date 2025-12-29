package good.damn.engine.opengl.models;

import android.util.Pair;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel;
import good.damn.engine.sdk.SDVector3;
import good.damn.engine.opengl.arrays.MGArrayVertexManager;
import good.damn.engine.opengl.entities.MGMaterial;

public final class MGMPoolMeshMutable {
    @NonNull public MGDrawerVertexArray drawerVertexArray;
    @NonNull public Pair<SDVector3, SDVector3> pointMinMax;
    @NonNull public SDVector3 pointMiddle;

    @NonNull
    public final MGMPoolVertexArray toImmutable() {
        return new MGMPoolVertexArray(
            drawerVertexArray,
            pointMinMax,
            pointMiddle
        );
    }
}
