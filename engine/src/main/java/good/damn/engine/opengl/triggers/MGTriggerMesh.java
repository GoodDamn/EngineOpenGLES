package good.damn.engine.opengl.triggers;

import android.opengl.GLES30;
import android.util.Pair;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;

import good.damn.engine.opengl.MGArrayVertex;
import good.damn.engine.opengl.MGObject3d;
import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals;
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity;
import good.damn.engine.opengl.drawers.MGDrawerMeshTexture;
import good.damn.engine.opengl.drawers.MGDrawerTexture;
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
    public final MGDrawerMeshTexture mesh;

    @NonNull
    public final MGDrawerTriggerStateable triggerState;

    private MGTriggerMesh(
        @NonNull final MGMatrixTriggerMesh matrix,
        @NonNull final MGDrawerMeshTexture mesh,
        @NonNull final MGDrawerTriggerStateable triggerState
    ) {
        this.matrix = matrix;
        this.mesh = mesh;
        this.triggerState = triggerState;
    }

    @NonNull
    public static MGTriggerMesh createFromObject(
        @NonNull final MGObject3d obj,
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGPoolTextures poolTextures,
        @NonNull final MGShaderSingleMode shaderWireframe,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        final MGArrayVertex arrayVertex = new MGArrayVertex();
        arrayVertex.configure(
            obj.vertices,
            obj.indices,
            MGArrayVertex.STRIDE
        );

        @NonNull final MGShaderMaterial mat = shaderDefault
            .getMaterial();

        @NonNull final MGMaterial material = MGMaterial.Companion.createWithPath(
            mat,
            poolTextures,
            obj.texturesDiffuseFileName == null ? null : obj.texturesDiffuseFileName[0],
            obj.texturesMetallicFileName == null ? null : obj.texturesMetallicFileName[0],
            obj.texturesEmissiveFileName == null ? null : obj.texturesEmissiveFileName[0]
        );

        return createFromVertexArray(
            arrayVertex,
            shaderDefault,
            shaderWireframe,
            material,
            outPoolMesh,
            triggerAction
        );
    }

    @NonNull
    public static MGTriggerMesh createFromVertexArray(
        @NonNull final MGArrayVertex vertexArray,
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGShaderSingleMode shaderWireframe,
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
            shaderDefault,
            outPoolMesh.toImmutable(),
            triggerAction,
            shaderWireframe
        );
    }

    @NonNull
    public static MGTriggerMesh createFromMeshPool(
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGMPoolMesh poolMesh,
        @NonNull final MGITrigger triggerAction,
        @NonNull final MGShaderSingleMode shaderWireframe
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
                new MGMatrixScaleRotation(),
                shaderDefault
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
                shaderDefault,
                matrix.matrixMesh.model
            ),
            GLES30.GL_CW,
            matrix.matrixMesh.normal
        );

        @NonNull
        final MGDrawerMeshTexture meshTexture = new MGDrawerMeshTexture(
            new MGDrawerTexture(
                material,
                material.getTextureDiffuse(),
                material.getTextureMetallic(),
                material.getTextureEmissive(),
                drawerMeshSwitchNormals
            ),
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
            shaderWireframe,
            matrix.matrixTrigger.model
        );

        return new MGTriggerMesh(
            matrix,
            meshTexture,
            triggerState
        );
    }
}
