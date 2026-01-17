package good.damn.engine.opengl.triggers;

import android.opengl.GLES30;
import android.util.Pair;

import androidx.annotation.NonNull;

import good.damn.common.COHandlerGl;
import good.damn.engine.models.MGMInformator;
import good.damn.common.vertex.COMArrayVertexManager;
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator;
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.MGObject3d;
import good.damn.engine.sdk.SDVector3;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals;
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity;
import good.damn.common.matrices.COMatrixScaleRotation;
import good.damn.common.matrices.COMatrixTransformationInvert;
import good.damn.common.matrices.COMatrixTransformationNormal;
import good.damn.engine.opengl.models.MGMPoolVertexArray;
import good.damn.engine.opengl.models.MGMPoolMeshMutable;
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerStateCallback;
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;
import good.damn.engine.opengl.runnables.misc.MGRunglConfigVertexArray;
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
    public static MGMPoolVertexArray createFromObject(
        @NonNull final MGObject3d obj,
        @NonNull final MGMInformator informator
    ) {
        @NonNull
        final MGArrayVertexConfigurator arrayVertex = new MGArrayVertexConfigurator(
            obj.config
        );

        @NonNull
        final COHandlerGl glHandler = informator
            .getGlHandler();

        glHandler.post(
            new MGRunglConfigVertexArray(
                arrayVertex,
                obj.vertices,
                obj.indices,
                MGPointerAttribute.defaultNoTangent
            )
        );

        return createFromVertexArray(
            new COMArrayVertexManager(
                obj.vertices
            ),
            arrayVertex
        );
    }

    @NonNull
    public static MGMPoolVertexArray createFromVertexArray(
        @NonNull final COMArrayVertexManager manager,
        @NonNull final MGArrayVertexConfigurator vertexArray
    ) {
        @NonNull
        final MGMPoolMeshMutable poolMesh = new MGMPoolMeshMutable();

        poolMesh.pointMinMax = MGUtilsAlgo.findMinMaxPoints(
            manager
        );

        poolMesh.pointMiddle = poolMesh.pointMinMax.first.interpolate(
            poolMesh.pointMinMax.second,
            0.5f
        );

        MGUtilsAlgo.offsetAnchorPoint(
            manager,
            vertexArray,
            poolMesh.pointMiddle
        );

        poolMesh.drawerVertexArray = new MGDrawerVertexArray(
            vertexArray
        );

        return poolMesh.toImmutable();
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
            new COMatrixTransformationInvert<>(
                new COMatrixScaleRotation()
            ),
            new COMatrixTransformationNormal<>(
                new COMatrixScaleRotation()
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
