package good.damn.opengles_engine.utils

import good.damn.opengles_engine.Application
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

class BufferUtils {
    companion object {
        fun allocateFloat(
            size: Int
        ): FloatBuffer {
             return ByteBuffer
                .allocateDirect(
                    size * 4
                ).order(
                     Application.BYTE_ORDER
                ).asFloatBuffer()
        }

        fun allocateInt(
            size: Int
        ): IntBuffer {
            return ByteBuffer
                .allocateDirect(
                    size * 4
                ).order(
                    Application.BYTE_ORDER
                ).asIntBuffer()
        }

        fun allocateShort(
            size: Int
        ): ShortBuffer {
            return ByteBuffer
                .allocateDirect(
                    size * 2
                ).order(
                    Application.BYTE_ORDER
                ).asShortBuffer()
        }

        fun createFloat(
            i: FloatArray
        ): FloatBuffer {
            val b = ByteBuffer
                .allocateDirect(
                    i.size * 4
                ).order(
                    Application.BYTE_ORDER
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
                    Application.BYTE_ORDER
                ).asShortBuffer()
                .put(i)
            b.position(0)
            return b
        }

    }
}