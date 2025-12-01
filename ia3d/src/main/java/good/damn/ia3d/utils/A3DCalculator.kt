package good.damn.ia3d.utils

import good.damn.ia3d.extractors.A3DIExtractor
import good.damn.ia3d.misc.A3DMVector2
import good.damn.ia3d.misc.A3DMVector3
import java.nio.ShortBuffer
import kotlin.math.tan

internal object A3DCalculator {

    @JvmStatic
    internal fun calculateTangentBitangent(
        positions: FloatArray,
        uv: FloatArray,
        indicesExtractor: A3DIExtractor
    ): FloatArray {

        val indicesCount = indicesExtractor
            .indicesCount

        val tangent = FloatArray(
            positions.size
        )

        val position0 = A3DMVector3()
        val position1 = A3DMVector3()
        val position2 = A3DMVector3()

        val uv0 = A3DMVector2()
        val uv1 = A3DMVector2()
        val uv2 = A3DMVector2()

        val edge1 = A3DMVector3()
        val edge2 = A3DMVector3()

        val dtuv1 = A3DMVector2()
        val dtuv2 = A3DMVector2()

        var i = 0
        while (i < indicesCount) {
            indicesExtractor.extract(i)
            val vertex0Index = indicesExtractor.vertex0
            val vertex1Index = indicesExtractor.vertex1
            val vertex2Index = indicesExtractor.vertex2

            val beginIndex0 = vertex0Index * 3
            val beginIndex1 = vertex1Index * 3
            val beginIndex2 = vertex2Index * 3

            // Positions
            getPositionAt(
                position0,
                positions,
                beginIndex0
            )

            getPositionAt(
                position1,
                positions,
                beginIndex1
            )

            getPositionAt(
                position2,
                positions,
                beginIndex2
            )

            // UVs
            getUvAt(
                uv0, uv,
                vertex0Index
            )

            getUvAt(
                uv1, uv,
                vertex1Index
            )

            getUvAt(
                uv2, uv,
                vertex2Index
            )

            edge1.subtract(
                position1,
                position0
            )

            edge2.subtract(
                position2,
                position0
            )

            dtuv1.subtract(
                uv1, uv0
            )

            dtuv2.subtract(
                uv2, uv0
            )


            val f = 1.0f / (dtuv1.x * dtuv2.y - dtuv2.x * dtuv1.y)
            val tangentX = f * (dtuv2.y * edge1.x - dtuv1.y * edge2.x)
            val tangentY = f * (dtuv2.y * edge1.y - dtuv1.y * edge2.y)
            val tangentZ = f * (dtuv2.y * edge1.z - dtuv1.y * edge2.z)

            calculateTangent(
                tangent,
                beginIndex0,
                tangentX, tangentY, tangentZ
            )

            calculateTangent(
                tangent,
                beginIndex1,
                tangentX, tangentY, tangentZ
            )

            calculateTangent(
                tangent,
                beginIndex2,
                tangentX, tangentY, tangentZ
            )

            //bitangent[iTangent]   = f * (-dtuv2.x * edge1.x + dtuv1.x * edge2.x)
            //bitangent[iTangent+1] = f * (-dtuv2.x * edge1.y + dtuv1.x * edge2.y)
            //bitangent[iTangent+2] = f * (-dtuv2.x * edge1.z + dtuv1.x * edge2.z)
            i += 3
        }

        i = 0
        val vector = A3DMVector3()
        while (i < tangent.size) {
            vector.x = tangent[i]
            vector.y = tangent[i+1]
            vector.z = tangent[i+2]
            vector.normalize()

            tangent[i] = vector.x
            tangent[i+1] = vector.y
            tangent[i+2] = vector.z

            i += 3
        }

        return tangent
    }

    private inline fun calculateTangent(
        tangent: FloatArray,
        beginIndex: Int,
        tangentX: Float,
        tangentY: Float,
        tangentZ: Float
    ) {
        tangent[
            beginIndex
        ] += tangentX

        tangent[
            beginIndex+1
        ] += tangentY

        tangent[
            beginIndex+2
        ] += tangentZ
    }

    private inline fun getPositionAt(
        out: A3DMVector3,
        positions: FloatArray,
        index: Int
    ) {
        out.x = positions[index]
        out.y = positions[index+1]
        out.z = positions[index+2]
    }

    private inline fun getUvAt(
        out: A3DMVector2,
        uv: FloatArray,
        index: Int
    ) {
        val i = index * 2
        out.x = uv[i]
        out.y = uv[i+1]
    }

}