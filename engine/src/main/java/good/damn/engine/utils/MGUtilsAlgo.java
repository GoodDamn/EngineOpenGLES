package good.damn.engine.utils;

import android.util.Pair;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.MGArrayVertex;
import good.damn.engine.opengl.MGVector;

public final class MGUtilsAlgo {

    public static Pair<MGVector, MGVector> findMinMaxPoints(
        @NonNull MGArrayVertex vertices
    ) {
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        float maxZ = Float.MIN_VALUE;

        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float minZ = Float.MAX_VALUE;

        float curX;
        float curY;
        float curZ;

        int count = vertices.getCountVertices();
        for (int i = 0; i < count; i++) {
            curX = vertices.getVertexBufferData(
                i, MGArrayVertex.INDEX_POSITION_X
            );
            if (curX > maxX) {
                maxX = curX;
            }

            if (curX < minX) {
                minX = curX;
            }


            curY = vertices.getVertexBufferData(
                i, MGArrayVertex.INDEX_POSITION_Y
            );
            if (curY > maxY) {
                maxY = curY;
            }

            if (curY < minY) {
                minY = curY;
            }



            curZ = vertices.getVertexBufferData(
                i, MGArrayVertex.INDEX_POSITION_Z
            );
            if (curZ > maxZ) {
                maxZ = curZ;
            }

            if (curZ < minZ) {
                minZ = curZ;
            }
        }

        return new Pair<>(
            new MGVector(
                minX, minY, minZ
            ),
            new MGVector(
                maxX, maxY, maxZ
            )
        );
    }

}
