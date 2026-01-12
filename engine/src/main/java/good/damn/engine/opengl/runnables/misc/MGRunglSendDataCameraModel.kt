package good.damn.engine.opengl.runnables.misc

import good.damn.common.COIRunnableBounds
import good.damn.engine.opengl.buffers.MGBufferUniformCamera
import java.nio.ByteBuffer

class MGRunglSendDataCameraModel(
    private val inputBuffer: ByteBuffer,
    private val buffer: MGBufferUniformCamera
): COIRunnableBounds {

    var isUpdated = true

    override fun run(
        width: Int,
        height: Int
    ) {
        synchronized(
            inputBuffer
        ) {
            buffer.setMatrixView(
                inputBuffer
            )
        }
        isUpdated = true
    }
}