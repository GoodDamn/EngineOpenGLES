package good.damn.apigl.buffers

import android.opengl.GLES30
import java.nio.ByteBuffer

class GLBufferUniformCamera(
    val buffer: GLBuffer
) {
    companion object {
        private const val SIZE_MAT44 = 64
    }

    val sizeBytes = 2 * SIZE_MAT44

    fun setMatrixProjection(
        data: ByteBuffer
    ) {
        bufferSubData(
            0,
            SIZE_MAT44,
            data
        )
    }

    fun setMatrixView(
        data: ByteBuffer
    ) {
        bufferSubData(
            SIZE_MAT44,
            SIZE_MAT44,
            data
        )
    }

    private inline fun bufferSubData(
        offset: Int,
        size: Int,
        data: ByteBuffer
    ) {
        buffer.bind()
        GLES30.glBufferSubData(
            GLES30.GL_UNIFORM_BUFFER,
            offset,
            size,
            data
        )
        buffer.unbind()
    }
}