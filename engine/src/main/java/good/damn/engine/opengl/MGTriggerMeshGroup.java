package good.damn.engine.opengl;

import android.opengl.GLES30;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque;
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.enums.MGEnumTextureType;
import good.damn.engine.opengl.pools.MGPoolTextures;
import good.damn.engine.opengl.shaders.MGIShaderTexture;
import good.damn.engine.opengl.shaders.MGShaderDefault;
import good.damn.engine.opengl.shaders.MGShaderMaterial;
import good.damn.engine.opengl.shaders.MGShaderSingleMode;
import good.damn.engine.opengl.textures.MGTexture;
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

    public static MGTriggerMeshGroup createFromObjects(
        @NonNull final MGObject3d[] objs,
        @NonNull final MGDrawerVertexArray drawVertBox,
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGShaderSingleMode shaderWireframe,
        @NonNull final MGITrigger triggerAction,
        @NonNull final MGPoolTextures poolTextures
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
                shaderDefault,
                poolTextures,
                drawVertBox,
                shaderWireframe,
                triggerAction
            );
        }

        return createFromTriggerMeshes(
            triggerMeshes
        );
    }
}
