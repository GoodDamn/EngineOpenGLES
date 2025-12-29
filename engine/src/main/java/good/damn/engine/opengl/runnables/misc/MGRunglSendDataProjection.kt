package good.damn.engine.opengl.runnables.misc

import good.damn.engine.opengl.buffers.MGBufferUniformCamera
import good.damn.engine.opengl.runnables.MGIRunnableBounds
import java.nio.ByteBuffer
import java.nio.FloatBuffer

class MGRunglSendDataProjection(
    private val inputBuffer: ByteBuffer,
    private val buffer: MGBufferUniformCamera
): MGIRunnableBounds {

    override fun run(
        width: Int,
        height: Int
    ) {
        buffer.setMatrixProjection(
            inputBuffer
        )
    }

}