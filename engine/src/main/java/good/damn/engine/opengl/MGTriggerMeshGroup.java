package good.damn.engine.opengl;

import androidx.annotation.NonNull;

import good.damn.engine.models.MGMInformator;
import good.damn.engine.opengl.models.MGMPoolVertexArray;
import good.damn.engine.opengl.models.MGMPoolMeshMutable;
import good.damn.engine.opengl.objects.MGObject3d;
import good.damn.engine.opengl.triggers.MGITrigger;
import good.damn.engine.opengl.triggers.MGMatrixTriggerGroup;
import good.damn.engine.opengl.triggers.MGMatrixTriggerMesh;
import good.damn.engine.opengl.triggers.MGTriggerMesh;

public final class MGTriggerMeshGroup {

    @NonNull
    public final MGMatrixTriggerGroup matrix;

    @NonNull
    public final MGTriggerMesh[] meshes;

    public MGTriggerMeshGroup(
        @NonNull final MGMatrixTriggerGroup matrix,
        @NonNull final MGTriggerMesh[] meshes
    ) {
        this.matrix = matrix;
        this.meshes = meshes;
    }

    public static MGTriggerMeshGroup createFromTriggerMeshes(
        @NonNull final MGTriggerMesh[] meshes
    ) {
        @NonNull
        final MGMatrixTriggerMesh[] matrices = new MGMatrixTriggerMesh[
            meshes.length
        ];

        for (
            int i = 0;
            i < matrices.length;
            i++
        ) { matrices[i] = meshes[i].matrix; }

        return new MGTriggerMeshGroup(
            MGMatrixTriggerGroup.createFromMatrices(
                matrices
            ),
            meshes
        );
    }

    /*@NonNull
    public static MGTriggerMeshGroup createFromPool(
        @NonNull final MGMPoolVertexArray[] poolMeshes,
        @NonNull final MGITrigger triggerAction
    ) {
        @NonNull
        final MGTriggerMesh[] triggerMeshes = new MGTriggerMesh[
            poolMeshes.length
        ];

        for (
            int i = 0;
            i < triggerMeshes.length;
            i++
        ) {
            triggerMeshes[i] = MGTriggerMesh.createFromMeshPool(
                poolMeshes[i],
                triggerAction
            );
        }

        return createFromTriggerMeshes(
            triggerMeshes
        );
    }

    public static MGTriggerMeshGroup createFromObjects(
        @NonNull final MGObject3d[] objs,
        @NonNull final MGMPoolMeshMutable[] outPoolMeshes,
        @NonNull final MGITrigger triggerAction,
        @NonNull final MGMInformator informator
    ) {
        @NonNull
        final MGTriggerMesh[] triggerMeshes = new MGTriggerMesh[
            objs.length
        ];

        @NonNull MGObject3d obj;
        for (
            int i = 0;
            i < triggerMeshes.length;
            i++
        ) {
            obj = objs[i];
            triggerMeshes[i] = MGTriggerMesh.createFromObject(
                obj,
                informator,
                outPoolMeshes[i],
                triggerAction
            );
        }

        return createFromTriggerMeshes(
            triggerMeshes
        );
    }*/
}
