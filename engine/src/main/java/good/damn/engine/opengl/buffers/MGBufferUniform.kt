package good.damn.engine.opengl.buffers

import android.opengl.GLES30
import java.nio.Buffer

object MGBufferUniform {

    @JvmStatic
    fun setupBindingPoint(
        buffer: MGBuffer,
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
            0,
            buffer.id,
            0,
            size
        )
    }
}