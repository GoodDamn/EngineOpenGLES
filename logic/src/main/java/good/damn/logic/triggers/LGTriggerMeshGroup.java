package good.damn.logic.triggers;

import androidx.annotation.NonNull;

/*public final class LGTriggerMeshGroup {

    @NonNull
    public final LGMatrixTriggerGroup matrix;

    @NonNull
    public final LGTriggerMesh[] meshes;

    public LGTriggerMeshGroup(
        @NonNull final LGMatrixTriggerGroup matrix,
        @NonNull final LGTriggerMesh[] meshes
    ) {
        this.matrix = matrix;
        this.meshes = meshes;
    }

    public static LGTriggerMeshGroup createFromTriggerMeshes(
        @NonNull final LGTriggerMesh[] meshes
    ) {
        @NonNull
        final LGMatrixTriggerMesh[] matrices = new LGMatrixTriggerMesh[
            meshes.length
        ];

        for (
            int i = 0;
            i < matrices.length;
            i++
        ) { matrices[i] = meshes[i].matrix; }

        return new LGTriggerMeshGroup(
            LGMatrixTriggerGroup.createFromMatrices(
                matrices
            ),
            meshes
        );
    }

    @NonNull
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
    }
}*/
