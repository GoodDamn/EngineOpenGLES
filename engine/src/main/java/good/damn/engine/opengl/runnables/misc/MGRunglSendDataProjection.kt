package good.damn.engine.opengl.runnables.misc

import good.damn.common.COIRunnableBounds
import good.damn.engine.opengl.buffers.MGBufferUniformCamera
import good.damn.common.MGIRunnableBounds
import java.nio.ByteBuffer

class MGRunglSendDataProjection(
    private val inputBuffer: ByteBuffer,
    private val buffer: MGBufferUniformCamera
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