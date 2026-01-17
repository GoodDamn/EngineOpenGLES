package good.damn.apigl.buffers

import android.opengl.GLES30

object GLBufferUniform {

    @JvmStatic
    fun setupBindingPoint(
        index: Int,
        buffer: GLBuffer,
        size: Int
    ) {
        buffer.bind()
        GLES30.glBufferData(
            GLES30.GL_UNIFORM_BUFFER,
            size,
            null,
            GLES30.GL_STATIC_DRAW
        )
        buffer.unbind()

        GLES30.glBindBufferRange(
            GLES30.GL_UNIFORM_BUFFER,
            index,
            buffer.id,
            0,
            size
        )
    }
}