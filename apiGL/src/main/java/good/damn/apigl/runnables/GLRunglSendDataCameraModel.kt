package good.damn.apigl.runnables

import good.damn.apigl.buffers.GLBufferUniformCamera
import good.damn.common.COIRunnableBounds
import java.nio.ByteBuffer

class GLRunglSendDataCameraModel(
    private val inputBuffer: ByteBuffer,
    private val buffer: GLBufferUniformCamera
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