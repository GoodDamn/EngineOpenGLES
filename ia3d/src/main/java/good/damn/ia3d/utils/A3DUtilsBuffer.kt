package good.damn.ia3d.utils

import good.damn.ia3d.enums.A3DEnumTypeBufferVertex
import good.damn.ia3d.extractors.A3DExtractorByte
import good.damn.ia3d.extractors.A3DExtractorShort
import good.damn.ia3d.models.A3DMBufferVertex
import good.damn.ia3d.models.A3DMConfigIndices
import good.damn.ia3d.stream.A3DInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal object A3DUtilsBuffer {

    internal val BYTE_ORDER = ByteOrder.nativeOrder()

    internal inline fun createBufferDynamic(
        indicesSize: Int,
        vertexBuffers: Array<A3DMBufferVertex?>,
        stream: A3DInputStream,
        vertexCount: Int
    ): A3DMConfigIndices {

        val positions = vertexBuffers[
            A3DEnumTypeBufferVertex.POSITION.type - 1
        ]!!.vertices

        val uv = vertexBuffers[
            A3DEnumTypeBufferVertex.UV1.type - 1
        ]!!.vertices

        if (vertexCount > Byte.MAX_VALUE.toInt() and 0xff) {
            val buffer = ByteBuffer.allocateDirect(
                indicesSize * 2
            ).order(
                BYTE_ORDER
            ).asShortBuffer().apply {
                fillBuffer(
                    indicesSize
                ) { put(it, stream.readLUShort().toShort()) }
            }

            val tangentBi = A3DCalculator.calculateTangentBitangent(
                positions,
                uv,
                A3DExtractorShort(
                    buffer
                )
            )

            return A3DMConfigIndices(
                2,
                buffer,
                tangentBi
            )
        }

        val buffer = ByteBuffer.allocateDirect(
            indicesSize
        ).order(
            BYTE_ORDER
        ).apply {
            fillBuffer(
                indicesSize
            ) { put(it, stream.readLUShort().toByte()) }
        }

        val tangentBi = A3DCalculator.calculateTangentBitangent(
            positions,
            uv,
            A3DExtractorByte(
                buffer
            )
        )

        return A3DMConfigIndices(
            1,
            buffer,
            tangentBi
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
}