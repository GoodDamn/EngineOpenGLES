package good.damn.engine.utils;

import android.util.Pair;

import androidx.annotation.NonNull;

import java.nio.FloatBuffer;

import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator;
import good.damn.engine.sdk.SDVector3;
import good.damn.common.vertex.MGArrayVertexManager;

public final class MGUtilsAlgo {

    public static void offsetAnchorPoint(
        @NonNull final MGArrayVertexManager vertexArray,
        @NonNull final MGArrayVertexConfigurator configurator,
        @NonNull final SDVector3 dt
    ) {
        int index = 0;
        configurator.bindVertexBuffer();
        int c = vertexArray.getSizeVertexArray();

        int ix, iy, iz;
        while (index < c) {
            ix = index + MGArrayVertexManager.INDEX_POSITION_X;
            iy = index + MGArrayVertexManager.INDEX_POSITION_Y;
            iz = index + MGArrayVertexManager.INDEX_POSITION_Z;

            float x = vertexArray.get(ix);
            float y = vertexArray.get(iy);
            float z = vertexArray.get(iz);

            vertexArray.set(
                ix, x - dt.getX()
            );

            vertexArray.set(
                iy, y - dt.getY()
            );

            vertexArray.set(
                iz, z - dt.getZ()
            );

            index += MGArrayVertexManager.MAX_VALUES_PER_VERTICES;
        }

        configurator.sendVertexBufferData(
            vertexArray.getVertices()
        );
        configurator.unbind();
    }

    public static Pair<SDVector3, SDVector3> findMinMaxPoints(
        @NonNull final MGArrayVertexManager vertices
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
                i, MGArrayVertexManager.INDEX_POSITION_X
            );
            if (curX > maxX) {
                maxX = curX;
            }

            if (curX < minX) {
                minX = curX;
            }


            curY = vertices.getVertexBufferData(
                i, MGArrayVertexManager.INDEX_POSITION_Y
            );
            if (curY > maxY) {
                maxY = curY;
            }

            if (curY < minY) {
                minY = curY;
            }



            curZ = vertices.getVertexBufferData(
                i, MGArrayVertexManager.INDEX_POSITION_Z
            );
            if (curZ > maxZ) {
                maxZ = curZ;
            }

            if (curZ < minZ) {
                minZ = curZ;
            }
        }

        return new Pair<>(
            new SDVector3(
                minX, minY, minZ
            ),
            new SDVector3(
                maxX, maxY, maxZ
            )
        );
    }

}
