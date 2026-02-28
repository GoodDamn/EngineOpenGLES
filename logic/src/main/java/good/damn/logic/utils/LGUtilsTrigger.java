package good.damn.logic.utils;

import android.util.Pair;

import androidx.annotation.NonNull;

import good.damn.engine.sdk.matrices.COMatrixScaleRotation;
import good.damn.engine.sdk.matrices.COMatrixTransformationInvert;
import good.damn.engine.sdk.matrices.COMatrixTransformationNormal;
import good.damn.common.vertex.COMArrayVertexManager;
import good.damn.engine.ASObject3d;
import good.damn.engine.sdk.SDVector3;
import good.damn.logic.models.LGTriggerPoint;
import good.damn.logic.triggers.LGMatrixTriggerMesh;

public final class LGUtilsTrigger {
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
}
