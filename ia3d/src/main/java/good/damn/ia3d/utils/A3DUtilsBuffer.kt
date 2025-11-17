package good.damn.ia3d.utils

import good.damn.ia3d.models.A3DMConfigIndices
import good.damn.ia3d.stream.A3DInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

internal object A3DUtilsBuffer {

    internal val BYTE_ORDER = ByteOrder.nativeOrder()

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
}