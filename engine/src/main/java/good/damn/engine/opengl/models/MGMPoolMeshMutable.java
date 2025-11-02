package good.damn.engine.opengl.models;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import good.damn.engine.opengl.MGArrayVertex;
import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch;

public final class MGMPoolMeshMutable {
    @NonNull public MGArrayVertex vertexArray;
    @NonNull public Pair<MGVector, MGVector> pointMinMax;
    @NonNull public MGDrawerModeSwitch drawerMode;
    @NonNull public MGVector pointMiddle;

    public final MGMPoolMesh toImmutable() {
        return new MGMPoolMesh(
            vertexArray,
            pointMinMax,
            drawerMode,
            pointMiddle
        );
    }
}
