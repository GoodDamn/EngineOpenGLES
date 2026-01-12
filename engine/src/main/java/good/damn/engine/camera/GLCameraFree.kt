package good.damn.engine.camera

import good.damn.common.COHandlerGl
import good.damn.engine.opengl.buffers.MGBufferUniformCamera
import good.damn.common.camera.COCameraFree
import good.damn.common.camera.COICameraFree
import good.damn.engine.opengl.runnables.misc.MGRunglSendDataCameraModel
import good.damn.engine.utils.MGUtilsBuffer

class GLCameraFree(
    private val cameraFree: COCameraFree,
    private val handler: COHandlerGl,
    uniformBufferCamera: MGBufferUniformCamera
): COICameraFree {

    private val mBufferView = MGUtilsBuffer.allocateByte(
        16 * 4
    )

    private val mRunglSendCameraModel = MGRunglSendDataCameraModel(
        mBufferView,
        uniformBufferCamera
    )

    override fun invalidatePosition() {
        cameraFree.invalidatePosition()
        mBufferView.asFloatBuffer().run {
            put(cameraFree.modelMatrix.model)
            position(0)
        }

        if (mRunglSendCameraModel.isUpdated) {
            mRunglSendCameraModel.isUpdated = false
            handler.post(
                mRunglSendCameraModel
            )
        }
    }

    override fun addPosition(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    ) = cameraFree.addPosition(
        x, y, directionX, directionY
    )

    override fun addRotation(
        yaw: Float,
        pitch: Float
    ) = cameraFree.addRotation(
        yaw, pitch
    )
}