package good.damn.engine2.camera

import good.damn.apigl.buffers.GLBufferUniformCamera
import good.damn.common.COHandlerGl
import good.damn.common.camera.COICameraFree
import good.damn.common.matrices.COMatrixTranslate
import good.damn.engine.ASUtilsBuffer
import good.damn.engine.sdk.SDVector3

class GLCameraFree(
    val cameraFree: COICameraFree,
    private val handler: COHandlerGl,
    uniformBufferCamera: GLBufferUniformCamera
): COICameraFree {

    private val mBufferView = ASUtilsBuffer.allocateByte(
        16 * 4
    )

    private val mRunglSendCameraModel = good.damn.apigl.runnables.GLRunglSendDataCameraModel(
        mBufferView,
        uniformBufferCamera
    )

    override val direction: SDVector3
        get() = cameraFree.direction

    override val modelMatrix: COMatrixTranslate
        get() = cameraFree.modelMatrix

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
        pitch: Float,
        roll: Float
    ) = cameraFree.addRotation(
        yaw, pitch, roll
    )
}