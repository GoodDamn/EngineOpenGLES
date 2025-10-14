package good.damn.engine.opengl.triggers;

import android.opengl.GLES30;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.MGArrayVertex;
import good.damn.engine.opengl.MGVector;
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch;
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch;
import good.damn.engine.opengl.entities.MGMesh;
import good.damn.engine.opengl.matrices.MGMatrixScale;
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal;
import good.damn.engine.opengl.shaders.MGIShaderNormal;
import good.damn.engine.opengl.shaders.MGShaderDefault;
import good.damn.engine.opengl.shaders.MGShaderSingleMode;
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox;
import good.damn.engine.utils.MGUtilsAlgo;

public final class MGTriggerMesh {

    @NonNull
    public final MGMatrixTriggerMesh matrix;

    @NonNull
    public final MGMesh mesh;

    @NonNull
    public final MGDrawerTriggerStateable triggerState;

    private MGTriggerMesh(
        @NonNull final MGMatrixTriggerMesh matrix,
        @NonNull final MGMesh mesh,
        @NonNull final MGDrawerTriggerStateable triggerState
    ) {
        this.matrix = matrix;
        this.mesh = mesh;
        this.triggerState = triggerState;
    }

    @NonNull
    public static MGTriggerMesh createFromVertexArray(
        @NonNull final MGArrayVertex vertexArray,
        @NonNull final MGArrayVertex vertexArrayBox,
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGShaderSingleMode shaderWireframe,
        @NonNull final MGDrawerModeSwitch drawerModeSwitch,
        @NonNull final MGITrigger triggerAction
    )  {
        @NonNull
        final Pair<
            MGVector, MGVector
        > pointMinMax = MGUtilsAlgo.findMinMaxPoints(
            vertexArray
        );

        @NonNull
        final MGVector pointMiddle = pointMinMax.first.interpolate(
            pointMinMax.second,
            0.5f
        );

        MGUtilsAlgo.offsetAnchorPoint(
            vertexArray,
            pointMiddle
        );

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

        @NonNull
        final MGMesh mesh = new MGMesh(
            drawerModeSwitch,
            shaderDefault,
            matrix.matrixMesh.model,
            matrix.matrixMesh.normal
        );

        @NonNull
        final MGDrawerTriggerStateable triggerState = new MGDrawerTriggerStateable(
            new MGManagerTriggerState(
                new MGTriggerMethodBox(
                    matrix.matrixTrigger.invert
                ),
                triggerAction
            ),
            vertexArrayBox,
            shaderWireframe,
            matrix.matrixTrigger.model
        );

        return new MGTriggerMesh(
            matrix,
            mesh,
            triggerState
        );
    }
}
