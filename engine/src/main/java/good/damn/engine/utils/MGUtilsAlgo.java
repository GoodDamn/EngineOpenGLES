package good.damn.engine.utils;

import android.util.Pair;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.MGArrayVertex;
import good.damn.engine.opengl.MGVector;

public final class MGUtilsAlgo {

    public static void offsetAnchorPoint(
        @NonNull MGArrayVertex vertexArray,
        @NonNull MGVector dt
    ) {
        int index = 0;
        vertexArray.bindVertexBuffer();
        int c = vertexArray.getSizeVertexArray();

        int ix, iy, iz;
        while (index < c) {
            ix = index + MGArrayVertex.INDEX_POSITION_X;
            iy = index + MGArrayVertex.INDEX_POSITION_Y;
            iz = index + MGArrayVertex.INDEX_POSITION_Z;

            float x = vertexArray.get(ix);
            float y = vertexArray.get(iy);
            float z = vertexArray.get(iz);

            vertexArray.writeVertexBufferData(
                ix, x - dt.getX()
            );

            vertexArray.writeVertexBufferData(
                iy, y - dt.getY()
            );

            vertexArray.writeVertexBufferData(
                iz, z - dt.getZ()
            );

            index += MGArrayVertex.MAX_VALUES_PER_VERTICES;
        }

        vertexArray.sendVertexBufferData();
        vertexArray.unbindVertexBuffer();
    }

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
