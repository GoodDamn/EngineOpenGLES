package good.damn.ia3d.utils

import good.damn.ia3d.misc.A3DMVector2
import good.damn.ia3d.misc.A3DMVector3
import good.damn.ia3d.models.A3DMConfigIndices
import good.damn.ia3d.stream.A3DInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal object A3DUtilsBuffer {

    internal val BYTE_ORDER = ByteOrder.nativeOrder()

    internal fun calculateTangentBitangent(
        positions: FloatArray,
        uv: FloatArray,
        indices: Array<Number>
    ): Pair<FloatArray, FloatArray> {

        val tangent = FloatArray(
            positions.size
        )

        val bitangent = FloatArray(
            tangent.size
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
        while (i < indices.size) {
            val vertex0Index = indices[i].toInt()
            val vertex1Index = indices[i+1].toInt()
            val vertex2Index = indices[i+2].toInt()

            // Positions
            getPositionAt(
                position0,
                positions,
                vertex0Index
            )

            getPositionAt(
                position1,
                positions,
                vertex1Index
            )

            getPositionAt(
                position2,
                positions,
                vertex2Index
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

            tangent[i]   = f * (dtuv2.y * edge1.x - dtuv1.y * edge2.x)
            tangent[i+1] = f * (dtuv2.y * edge1.y - dtuv1.y * edge2.y)
            tangent[i+2] = f * (dtuv2.y * edge1.z - dtuv1.y * edge2.z)

            bitangent[i]   = f * (-dtuv2.x * edge1.x + dtuv1.x * edge2.x)
            bitangent[i+1] = f * (-dtuv2.x * edge1.y + dtuv1.x * edge2.y)
            bitangent[i+2] = f * (-dtuv2.x * edge1.z + dtuv1.x * edge2.z)

            i += 3
        }

        return Pair(
            tangent,
            bitangent
        )
    }

    internal inline fun createBufferDynamic(
        indicesSize: Int,
        stream: A3DInputStream,
        vertexCount: Int
    ): A3DMConfigIndices {
        if (vertexCount > Byte.MAX_VALUE.toInt() and 0xff) {
            return A3DMConfigIndices(
                2,
                ByteBuffer.allocateDirect(
                    indicesSize * 2
                ).order(
                    BYTE_ORDER
                ).asShortBuffer().apply {
                    fillBuffer(
                        indicesSize
                    ) { put(it, stream.readLUShort().toShort()) }
                }
            )
        }

        return A3DMConfigIndices(
            1,
            ByteBuffer.allocateDirect(
                indicesSize
            ).order(
                BYTE_ORDER
            ).apply {
                fillBuffer(
                    indicesSize
                ) { put(it, stream.readLUShort().toByte()) }
            }
        )
    }

    private inline fun fillBuffer(
        indexCount: Int,
        call: (Int) -> Unit
    ) {
        for (i in 0 until indexCount) {
            call(i)
        }
    }

    private fun getPositionAt(
        out: A3DMVector3,
        positions: FloatArray,
        index: Int
    ) {
        val i = index * 3
        out.x = positions[i]
        out.y = positions[i+1]
        out.z = positions[i+2]
    }

    private fun getUvAt(
        out: A3DMVector2,
        uv: FloatArray,
        index: Int
    ) {
        val i = index * 2
        out.x = uv[i]
        out.y = uv[i+1]
    }
}