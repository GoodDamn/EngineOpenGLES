package good.damn.engine.utils

import good.damn.engine.MGEngine
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

    }
}