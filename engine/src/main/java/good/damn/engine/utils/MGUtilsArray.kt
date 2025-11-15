package good.damn.engine.utils

import java.nio.FloatBuffer

object MGUtilsArray {

    fun createMergedVertexBuffer(
        position: FloatArray,
        uv: FloatArray,
        normal: FloatArray,
        uvScale: Float
    ): FloatBuffer {
        val vertexCount = position.size / 3
        val outSize = 8 * vertexCount
        val output = MGUtilsBuffer.allocateFloat(
            outSize
        )

        var iUv = 0
        var iNormal = 0
        var iPosition = 0

        var ii = 0
        while (ii < outSize) {
            // Position
            output.put(
                ii++,
                position[iPosition++]
            )

            output.put(
                ii++,
                position[iPosition+1]
            )

            output.put(
                ii++,
                position[iPosition]
            )
            iPosition += 2


            // UVs
            output.put(
                ii++,
                uv[iUv++] * uvScale
            )
            output.put(
                ii++,
                uv[iUv++] * uvScale
            )


            // Normals
            output.put(
                ii++,
                normal[iNormal++]
            )

            output.put(
                ii++,
                normal[iNormal+1]
            )

            output.put(
                ii++,
                normal[iNormal]
            )

            iNormal += 2
        }

        output.position(0)
        return output
    }
}