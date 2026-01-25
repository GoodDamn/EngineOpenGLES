package good.damn.engine

import android.util.Pair
import good.damn.apigl.enums.GLEnumArrayVertexConfiguration
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

object ASUtilsBuffer {
    private val BYTE_ORDER = ByteOrder.nativeOrder()

    @JvmStatic
    fun allocateByte(
        size: Int
    ) = ByteBuffer.allocateDirect(
        size
    ).order(
        BYTE_ORDER
    )

    @JvmStatic
    fun allocateFloat(
        size: Int
    ) = allocateByte(
        size * 4
    ).asFloatBuffer()

    @JvmStatic
    fun allocateInt(
        size: Int
    ) = allocateByte(
        size * 4
    ).asIntBuffer()

    @JvmStatic
    fun allocateShort(
        size: Int
    ) = allocateByte(
        size * 2
    ).asShortBuffer()

    @JvmStatic
    fun createByte(
        i: ByteArray
    ): ByteBuffer {
        val b = allocateByte(
            i.size
        ).put(i)
        b.position(0)
        return b
    }

    @JvmStatic
    fun createFloat(
        i: FloatArray
    ): FloatBuffer {
        val b = allocateFloat(
            i.size
        ).put(i)
        b.position(0)
        return b
    }

    @JvmStatic
    fun createShort(
        i: ShortArray
    ): ShortBuffer {
        val b = allocateShort(
            i.size
        ).put(i)
        b.position(0)
        return b
    }

    @JvmStatic
    fun createInt(
        i: IntArray
    ): IntBuffer {
        val b = allocateInt(
            i.size
        ).put(i)
        b.position(0)
        return b
    }

    @JvmStatic
    inline fun createBufferIndicesDynamic(
        indices: IntArray,
        vertexCount: Int
    ): Pair<
        GLEnumArrayVertexConfiguration,
        Buffer
    > {
        if (vertexCount > (Short.MAX_VALUE.toInt() and 0xffff)) {
            return Pair(
                GLEnumArrayVertexConfiguration.INT,
                allocateInt(
                    indices.size
                ).apply {
                    fillBuffer(
                        indices.size
                    ) { put(it, indices[it]) }
                }
            )
        }

        if (vertexCount > (Byte.MAX_VALUE.toInt() and 0xff)) {
            return Pair(
                GLEnumArrayVertexConfiguration.SHORT,
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
            GLEnumArrayVertexConfiguration.BYTE,
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