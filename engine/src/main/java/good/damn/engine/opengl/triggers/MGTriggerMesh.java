package good.damn.engine.opengl.triggers;

import android.opengl.GLES30;
import android.util.Pair;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;

import good.damn.engine.opengl.MGArrayVertex;
import good.damn.engine.opengl.MGObject3d;
import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals;
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch;
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal;
import good.damn.engine.opengl.models.MGMPoolMesh;
import good.damn.engine.opengl.models.MGMPoolMeshMutable;
import good.damn.engine.opengl.pools.MGPoolTextures;
import good.damn.engine.opengl.shaders.MGShaderDefault;
import good.damn.engine.opengl.shaders.MGShaderMaterial;
import good.damn.engine.opengl.shaders.MGShaderSingleMode;
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerStateCallback;
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;
import good.damn.engine.utils.MGUtilsAlgo;

public final class MGTriggerMesh {

    @NonNull
    public final MGMatrixTriggerMesh matrix;

    @NonNull
    public final MGDrawerMeshMaterialSwitch mesh;

    @NonNull
    public final MGDrawerTriggerStateable triggerState;

    private MGTriggerMesh(
        @NonNull final MGMatrixTriggerMesh matrix,
        @NonNull final MGDrawerMeshMaterialSwitch mesh,
        @NonNull final MGDrawerTriggerStateable triggerState
    ) {
        this.matrix = matrix;
        this.mesh = mesh;
        this.triggerState = triggerState;
    }

    @NonNull
    public static MGTriggerMesh createFromObject(
        @NonNull final MGObject3d obj,
        @NonNull final MGPoolTextures poolTextures,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        final MGArrayVertex arrayVertex = new MGArrayVertex();
        arrayVertex.configure(
            obj.vertices,
            obj.indices,
            MGArrayVertex.STRIDE
        );

        @NonNull final MGMaterial material = MGMaterial.Companion.createWithPath(
            poolTextures,
            obj.texturesDiffuseFileName == null ? null : obj.texturesDiffuseFileName[0],
            obj.texturesMetallicFileName == null ? null : obj.texturesMetallicFileName[0],
            obj.texturesEmissiveFileName == null ? null : obj.texturesEmissiveFileName[0]
        );

        return createFromVertexArray(
            arrayVertex,
            material,
            outPoolMesh,
            triggerAction
        );
    }

    @NonNull
    public static MGTriggerMesh createFromVertexArray(
        @NonNull final MGArrayVertex vertexArray,
        @NonNull final MGMaterial material,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        outPoolMesh.pointMinMax = MGUtilsAlgo.findMinMaxPoints(
            vertexArray
        );

        outPoolMesh.pointMiddle = outPoolMesh.pointMinMax.first.interpolate(
            outPoolMesh.pointMinMax.second,
            0.5f
        );

        MGUtilsAlgo.offsetAnchorPoint(
            vertexArray,
            outPoolMesh.pointMiddle
        );

        outPoolMesh.material = material;
        outPoolMesh.vertexArray = vertexArray;

        return createFromMeshPool(
            outPoolMesh.toImmutable(),
            triggerAction
        );
    }

    @NonNull
    public static MGTriggerMesh createFromMeshPool(
        @NonNull final MGMPoolMesh poolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        @NonNull final Pair<
            MGVector, MGVector
        > pointMinMax = poolMesh.getPointMinMax();

        @NonNull final MGVector pointMiddle = poolMesh
            .getPointMiddle();

        @NonNull
        final MGMatrixTriggerMesh matrix = new MGMatrixTriggerMesh(
            new MGMatrixTransformationInvert<>(
                new MGMatrixScaleRotation()
            ),
            new MGMatrixTransformationNormal<>(
                new MGMatrixScaleRotation()
            ),
            pointMinMax.first,
            pointMinMax.second
        );

        matrix.setPosition(
            pointMiddle.getX(),
            pointMiddle.getY(),
            pointMiddle.getZ()
        );

        matrix.invalidatePosition();
        matrix.invalidateScaleRotation();
        matrix.calculateInvertTrigger();
        matrix.calculateNormals();

        @NonNull
        final MGMaterial material = poolMesh.getMaterial();

        @NonNull
        final MGDrawerMeshSwitchNormals drawerMeshSwitchNormals = new MGDrawerMeshSwitchNormals(
            poolMesh.getVertexArray(),
            new MGDrawerPositionEntity(
                matrix.matrixMesh.model
            ),
            GLES30.GL_CW,
            matrix.matrixMesh.normal
        );

        @NonNull
        final MGDrawerMeshMaterialSwitch meshTexture = new MGDrawerMeshMaterialSwitch(
            material,
            drawerMeshSwitchNormals
        );

        @NonNull
        final MGDrawerTriggerStateable triggerState = new MGDrawerTriggerStateable(
            new MGManagerTriggerStateCallback(
                new MGTriggerMethodBox(
                    matrix.matrixTrigger.invert
                ),
                triggerAction
            ),
            matrix.matrixTrigger.model
        );

        return new MGTriggerMesh(
            matrix,
            meshTexture,
            triggerState
        );
    }
}
