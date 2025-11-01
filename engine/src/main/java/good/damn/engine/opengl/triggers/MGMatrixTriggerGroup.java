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

    private float mx = 0f;
    private float my = 0f;
    private float mz = 0f;

    @Override
    public void setPosition(
        float x,
        float y,
        float z
    ) {
        mx = x;
        my = y;
        mz = z;
        for (
            @NonNull final MGMWrapperMatrix matrix
            : matrices
        ) {
            matrix.matrix.setPosition(
                mx + matrix.translateX,
                my + matrix.translateY,
                mz + matrix.translateZ
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
        for (
            @NonNull final MGMWrapperMatrix matrix
            : matrices
        ) {
            matrix.matrix.setScale(
                x, y, z
            );

            matrix.translateX = x * matrix.groupX;
            matrix.translateY = y * matrix.groupY;
            matrix.translateZ = z * matrix.groupZ;
        }

        setPosition(
            mx, my, mz
        );
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
            matrix.matrix.calculateNormals();
        }
    }

    private static class MGMWrapperMatrix {
       final float groupX;
       final float groupY;
       final float groupZ;

        float translateX;
        float translateY;
        float translateZ;

        @NonNull
        final MGMatrixTriggerMesh matrix;

        MGMWrapperMatrix(
            @NonNull final MGMatrixTriggerMesh matrix
        ) {
            this.matrix = matrix;
            groupX = matrix.matrixMesh.model.getX();
            groupY = matrix.matrixMesh.model.getY();
            groupZ = matrix.matrixMesh.model.getZ();

            translateX = groupX;
            translateY = groupY;
            translateZ = groupZ;
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
