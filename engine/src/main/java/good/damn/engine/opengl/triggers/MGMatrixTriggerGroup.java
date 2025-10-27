package good.damn.engine.opengl.triggers;

import androidx.annotation.NonNull;

public final class MGMatrixTriggerGroup
implements MGIMatrixTrigger {

    @NonNull
    private final MGMWrapperMatrix[] matrices;

    private MGMatrixTriggerGroup(
        @NonNull final MGMWrapperMatrix[] matrices
    ) {
        this.matrices = matrices;
    }

    @Override
    public void setPosition(
        float x,
        float y,
        float z
    ) {
        for (
            @NonNull final MGMWrapperMatrix matrix
            : matrices
        ) {
            matrix.matrix.setPosition(
                x + matrix.groupX,
                y + matrix.groupY,
                z + matrix.groupZ
            );
        }
    }

    @Override
    public final void addPosition(
        float x,
        float y,
        float z
    ) {
        for (
            @NonNull final MGMWrapperMatrix matrix
            : matrices
        ) {
            matrix.matrix.addPosition(
                x, y, z
            );
        }
    }

    @Override
    public void setScale(
        float x,
        float y,
        float z
    ) {

    }

    @Override
    public void addRotation(
        float x,
        float y,
        float z
    ) {

    }

    @Override
    public final void invalidatePosition() {
        for (
            @NonNull final MGMWrapperMatrix matrix
            : matrices
        ) {
            matrix.matrix.invalidatePosition();
        }
    }

    @Override
    public final void invalidateScaleRotation() {
        for (
            @NonNull final MGMWrapperMatrix matrix
            : matrices
        ) {
            matrix.matrix.invalidateScaleRotation();
        }
    }

    @Override
    public final void calculateInvertTrigger() {
        for (
            @NonNull final MGMWrapperMatrix matrix
            : matrices
        ) {
            matrix.matrix.calculateInvertTrigger();
        }
    }

    @Override
    public final void calculateNormals() {
        for (
            @NonNull final MGMWrapperMatrix matrix
            : matrices
        ) {
            matrix.matrix.calculateNormalsMesh();
        }
    }

    private static class MGMWrapperMatrix {
        float groupX;
        float groupY;
        float groupZ;

        @NonNull
        final MGMatrixTriggerMesh matrix;

        MGMWrapperMatrix(
            @NonNull final MGMatrixTriggerMesh matrix
        ) {
            this.matrix = matrix;
            groupX = matrix.matrixMesh.model.getX();
            groupY = matrix.matrixMesh.model.getY();
            groupZ = matrix.matrixMesh.model.getZ();
        }
    }

    public static MGMatrixTriggerGroup createFromMatrices(
        @NonNull final MGMatrixTriggerMesh[] matrices
    ) {
        @NonNull final MGMWrapperMatrix[] wrappers = new MGMWrapperMatrix[
            matrices.length
        ];

        for (
            int i = 0;
            i < wrappers.length;
            i++
        ) {
            wrappers[i] = new MGMWrapperMatrix(
                matrices[i]
            );
        }

        return new MGMatrixTriggerGroup(
            wrappers
        );
    }
}
