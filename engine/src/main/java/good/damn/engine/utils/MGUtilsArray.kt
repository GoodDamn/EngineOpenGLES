package good.damn.engine.utils

import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import java.nio.FloatBuffer
import kotlin.math.tan

object MGUtilsArray {

    private const val PUNTB = MGArrayVertexConfigurator.MAX_VALUES_PER_VERTICES

    @JvmStatic
    fun createMergedVertexBuffer(
        position: FloatArray,
        uv: FloatArray,
        normal: FloatArray,
        tangent: FloatArray,
        uvScale: Float
    ): FloatBuffer {
        val vertexCount = position.size / 3
        val outSize = PUNTB * vertexCount
        val output = MGUtilsBuffer.allocateFloat(
            outSize
        )

        var iUv = 0
        var iNormal = 0
        var iPosition = 0
        var iTangent = 0

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


            // ----------------------------------------
            // Tangent
            output.put(
                ii++,
                tangent[iTangent++]
            )

            output.put(
                ii++,
                tangent[iTangent+1]
            )

            output.put(
                ii++,
                tangent[iTangent]
            )
            iTangent += 2
        }

        output.position(0)
        return output
    }
}