package good.damn.engine.opengl.triggers;

import android.opengl.GLES30;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

import good.damn.engine.loaders.MGLoaderLevelLibrary;
import good.damn.engine.loaders.texture.MGLoaderTextureAsync;
import good.damn.engine.models.MGMInformator;
import good.damn.engine.models.MGMInformatorShader;
import good.damn.engine.opengl.arrays.MGArrayVertexManager;
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.entities.MGMaterialTexture;
import good.damn.engine.opengl.enums.MGEnumTextureType;
import good.damn.engine.opengl.objects.MGObject3d;
import good.damn.engine.opengl.pools.MGPoolMaterials;
import good.damn.engine.opengl.shaders.MGShaderMaterial;
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel;
import good.damn.engine.opengl.shaders.MGShaderTexture;
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute;
import good.damn.engine.sdk.SDVector3;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals;
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity;
import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal;
import good.damn.engine.opengl.models.MGMPoolVertexArray;
import good.damn.engine.opengl.models.MGMPoolMeshMutable;
import good.damn.engine.opengl.thread.MGHandlerGl;
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerStateCallback;
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;
import good.damn.engine.runnables.MGRunnableConfigVertexArray;
import good.damn.engine.shader.MGShaderCache;
import good.damn.engine.shader.MGShaderSource;
import good.damn.engine.shader.generators.MGGeneratorMaterialG;
import good.damn.engine.utils.MGUtilsAlgo;

public final class MGTriggerMesh {

    @NonNull
    public final MGMatrixTriggerMesh matrix;

    @NonNull
    public final MGDrawerMeshSwitch mesh;

    @NonNull
    public final MGDrawerTriggerStateable triggerState;

    private MGTriggerMesh(
        @NonNull final MGMatrixTriggerMesh matrix,
        @NonNull final MGDrawerMeshSwitch mesh,
        @NonNull final MGDrawerTriggerStateable triggerState
    ) {
        this.matrix = matrix;
        this.mesh = mesh;
        this.triggerState = triggerState;
    }

    @NonNull
    public static MGTriggerMesh createFromObject(
        @NonNull final MGObject3d obj,
        @NonNull final MGMInformator informator,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        @NonNull
        final MGArrayVertexManager arrayVertex = new MGArrayVertexManager(
            obj.config
        );

        @NonNull
        final MGHandlerGl glHandler = informator
            .getGlHandler();

        glHandler.post(
            new MGRunnableConfigVertexArray(
                arrayVertex,
                obj.vertices,
                obj.indices,
                MGPointerAttribute.defaultNoTangent
            )
        );

        arrayVertex.keepBufferVertices(
            obj.vertices
        );

        return createFromVertexArray(
            arrayVertex,
            outPoolMesh,
            triggerAction
        );
    }

    @NonNull
    public static MGTriggerMesh createFromVertexArray(
        @NonNull final MGArrayVertexManager vertexArray,
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

        vertexArray.unkeepBufferVertices();
        outPoolMesh.drawerVertexArray = new MGDrawerVertexArray(
            vertexArray
        );

        return createFromMeshPool(
            outPoolMesh.toImmutable(),
            triggerAction
        );
    }

    @NonNull
    public static MGTriggerMesh createFromMeshPool(
        @NonNull final MGMPoolVertexArray poolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        @NonNull final Pair<
            SDVector3, SDVector3
        > pointMinMax = poolMesh.getPointMinMax();

        @NonNull final SDVector3 pointMiddle = poolMesh
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
        final MGDrawerMeshSwitchNormals drawerMeshSwitchNormals = new MGDrawerMeshSwitchNormals(
            poolMesh.getDrawerVertexArray(),
            new MGDrawerPositionEntity(
                matrix.matrixMesh.model
            ),
            GLES30.GL_CCW,
            matrix.matrixMesh.normal
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
            drawerMeshSwitchNormals,
            triggerState
        );
    }
}
