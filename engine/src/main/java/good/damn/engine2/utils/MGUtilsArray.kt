package good.damn.engine2.utils

import good.damn.engine.ASUtilsBuffer
import java.nio.FloatBuffer

object MGUtilsArray {

    private const val PUNTB = 11

    @JvmStatic
    fun swapIndexValues(
        vertices: FloatBuffer,
        indexSwap: Int,
        indexOn: Int,
        vertexValueCount: Int
    ) {
        val valuesCount = vertices.remaining()
        var swapValue: Float
        var currentIndexSwap = indexSwap
        var currentIndexOn = indexOn

        while (currentIndexSwap < valuesCount) {
            swapValue = vertices[
                currentIndexSwap
            ]

            vertices.put(
                currentIndexSwap,
                vertices[currentIndexOn]
            )

            vertices.put(
                currentIndexOn,
                swapValue
            )

            currentIndexSwap += vertexValueCount
            currentIndexOn += vertexValueCount
        }
    }

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
        val output = ASUtilsBuffer.allocateFloat(
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