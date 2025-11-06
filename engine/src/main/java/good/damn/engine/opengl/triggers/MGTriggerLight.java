package good.damn.engine.opengl.triggers;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.MGArrayVertex;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.entities.MGLight;
import good.damn.engine.opengl.matrices.MGMatrixInvert;
import good.damn.engine.opengl.matrices.MGMatrixScale;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.opengl.shaders.MGShaderDefault;
import good.damn.engine.opengl.shaders.MGShaderSingleMode;
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerState;
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodSphere;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight;

public final class MGTriggerLight {

    @NonNull
    public final MGMatrixTriggerLight matrix;

    @NonNull
    public final MGDrawerTriggerStateableLight triggerState;

    private MGTriggerLight(
        @NonNull final MGMatrixTriggerLight matrix,
        @NonNull final MGDrawerTriggerStateableLight triggerState
    ) {
        this.matrix = matrix;
        this.triggerState = triggerState;
    }

    @NonNull
    public static MGTriggerLight createFromLight(
        @NonNull final MGLight light,
        @NonNull final MGShaderSingleMode shaderWireframe
    ) {
        @NonNull
        final MGMatrixTriggerLight matrix = new MGMatrixTriggerLight(
            new MGMatrixTransformationInvert<>(
                new MGMatrixScale()
            )
        );

        @NonNull
        final MGDrawerTriggerStateableLight triggerState = new MGDrawerTriggerStateableLight(
            light,
            new MGManagerTriggerState(
                new MGTriggerMethodSphere(
                    matrix.matrixTrigger.invert
                )
            ),
            shaderWireframe,
            matrix
        );

        return new MGTriggerLight(
            matrix,
            triggerState
        );
    }
}
