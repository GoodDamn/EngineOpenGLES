package good.damn.engine.utils

import android.util.Pair
import good.damn.engine.MGEngine
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.ia3d.models.A3DMConfigIndices
import good.damn.ia3d.stream.A3DInputStream
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

class MGUtilsBuffer {
    companion object {

        fun allocateByte(
            size: Int
        ) = ByteBuffer.allocateDirect(
            size
        ).order(
            MGEngine.BYTE_ORDER
        )

        fun allocateFloat(
            size: Int
        ) = ByteBuffer.allocateDirect(
            size * 4
        ).order(
            MGEngine.BYTE_ORDER
        ).asFloatBuffer()

        fun allocateInt(
            size: Int
        ) = ByteBuffer.allocateDirect(
            size * 4
        ).order(
            MGEngine.BYTE_ORDER
        ).asIntBuffer()

        fun allocateShort(
            size: Int
        ) = ByteBuffer.allocateDirect(
            size * 2
        ).order(
            MGEngine.BYTE_ORDER
        ).asShortBuffer()

        fun createByte(
            i: ByteArray
        ): ByteBuffer {
            val b = ByteBuffer
                .allocateDirect(
                    i.size * 4
                ).order(
                    MGEngine.BYTE_ORDER
                ).put(i)
            b.position(0)
            return b
        }

        fun createFloat(
            i: FloatArray
        ): FloatBuffer {
            val b = ByteBuffer
                .allocateDirect(
                    i.size * 4
                ).order(
                    MGEngine.BYTE_ORDER
                ).asFloatBuffer()
                .put(i)
            b.position(0)
            return b
        }

        fun createShort(
            i: ShortArray
        ): ShortBuffer {
            val b = ByteBuffer
                .allocateDirect(
                    i.size * 2
                ).order(
                    MGEngine.BYTE_ORDER
                ).asShortBuffer()
                .put(i)
            b.position(0)
            return b
        }

        fun createInt(
            i: IntArray
        ): IntBuffer {
            val b = ByteBuffer
                .allocateDirect(
                    i.size * 4
                ).order(
                    MGEngine.BYTE_ORDER
                ).asIntBuffer()
                .put(i)
            b.position(0)
            return b
        }

        inline fun createBufferIndicesDynamic(
            indices: IntArray,
            vertexCount: Int
        ): Pair<MGEnumArrayVertexConfiguration, Buffer> {
            if (vertexCount > Short.MAX_VALUE.toInt() and 0xffff) {
                return Pair(
                    MGEnumArrayVertexConfiguration.INT,
                    allocateInt(
                        indices.size
                    ).apply {
                        fillBuffer(
                            indices.size
                        ) { put(it, indices[it]) }
                    }
                )
            }

            if (vertexCount > Byte.MAX_VALUE.toInt() and 0xff) {
                return Pair(
                    MGEnumArrayVertexConfiguration.SHORT,
                    allocateShort(
                        indices.size
                    ).apply {
                        fillBuffer(
                            indices.size
                        ) { put(it, indices[it].toShort()) }
                    }
                )
            }

            return Pair(
                MGEnumArrayVertexConfiguration.BYTE,
                allocateByte(
                    indices.size
                ).apply {
                    fillBuffer(
                        indices.size
                    ) { put(it, indices[it].toByte()) }
                }
            )
        }

        inline fun fillBuffer(
            indexCount: Int,
            call: (Int) -> Unit
        ) {
            for (i in 0 until indexCount) {
                call(i)
            }
        }

    }
}