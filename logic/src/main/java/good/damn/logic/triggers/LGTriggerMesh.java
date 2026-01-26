package good.damn.logic.triggers;

import android.util.Pair;

import androidx.annotation.NonNull;

import good.damn.common.matrices.COMatrixScaleRotation;
import good.damn.common.matrices.COMatrixTransformationInvert;
import good.damn.common.matrices.COMatrixTransformationNormal;
import good.damn.common.vertex.COMArrayVertexManager;
import good.damn.engine.ASObject3d;
import good.damn.engine.sdk.SDVector3;
import good.damn.logic.models.LGTriggerPoint;
import good.damn.logic.triggers.methods.LGTriggerMethodBox;
import good.damn.logic.utils.LGUtilsAlgo;

public final class LGTriggerMesh {

    @NonNull
    public final LGMatrixTriggerMesh matrix;

    private LGTriggerMesh(
        @NonNull final LGMatrixTriggerMesh matrix
    ) {
        this.matrix = matrix;
    }

    @NonNull
    public static LGTriggerPoint createTriggerPoint(
        @NonNull final ASObject3d obj
    ) {
        @NonNull
        final COMArrayVertexManager manager = new COMArrayVertexManager(
            obj.vertices
        );

        @NonNull
        final Pair<
            SDVector3,
            SDVector3
        > pointMinMax = LGUtilsAlgo.findMinMaxPoints(
            manager
        );

        @NonNull
        final SDVector3 pointMiddle = pointMinMax.first.interpolate(
            pointMinMax.second,
            0.5f
        );

        LGUtilsAlgo.offsetAnchorPoint(
            manager,
            pointMiddle
        );

        return new LGTriggerPoint(
            pointMinMax,
            pointMiddle
        );
    }

    @NonNull
    public static LGMatrixTriggerMesh createTriggerPointMatrix(
        @NonNull final LGTriggerPoint triggerPoint
    ) {
        @NonNull final Pair<
            SDVector3, SDVector3
            > pointMinMax = triggerPoint.getPointMinMax();

        @NonNull final SDVector3 pointMiddle = triggerPoint
            .getPointMiddle();

        @NonNull
        final LGMatrixTriggerMesh matrix = new LGMatrixTriggerMesh(
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

        return matrix;
    }

    @NonNull
    public static LGTriggerMesh createFromMatrix(
        @NonNull final LGMatrixTriggerMesh matrix
    ) {
        return new LGTriggerMesh(
            matrix
        );
    }
}
