package good.damn.engine2.camera

import good.damn.apigl.buffers.GLBufferUniformCamera
import good.damn.apigl.runnables.GLRunglSendDataCameraModel
import good.damn.common.COHandlerGl
import good.damn.common.camera.COICameraFree
import good.damn.engine.sdk.matrices.SDMatrixTranslate
import good.damn.engine.ASUtilsBuffer
import good.damn.engine.sdk.SDVector3

class GLCameraFree(
    private val modelMatrix: FloatArray,
    private val handler: COHandlerGl,
    uniformBufferCamera: GLBufferUniformCamera
) {

    private val mBufferView = ASUtilsBuffer.allocateByte(
        16 * 4
    )

    private val mRunglSendCameraModel = GLRunglSendDataCameraModel(
        mBufferView,
        uniformBufferCamera
    )

    fun invalidatePosition() {
        mBufferView.asFloatBuffer().run {
            put(modelMatrix)
            position(0)
        }

        if (mRunglSendCameraModel.isUpdated) {
            mRunglSendCameraModel.isUpdated = false
            handler.post(
                mRunglSendCameraModel
            )
        }
    }

}