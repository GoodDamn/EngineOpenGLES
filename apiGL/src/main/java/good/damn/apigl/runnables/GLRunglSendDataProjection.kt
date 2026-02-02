package good.damn.apigl.runnables

import good.damn.apigl.buffers.GLBufferUniformCamera
import good.damn.common.COIRunnableBounds
import java.nio.ByteBuffer

class GLRunglSendDataProjection(
    private val inputBuffer: ByteBuffer,
    private val buffer: GLBufferUniformCamera
): COIRunnableBounds {

    override fun run(
        width: Int,
        height: Int
    ) {
        buffer.setMatrixProjection(
            inputBuffer
        )
    }

}