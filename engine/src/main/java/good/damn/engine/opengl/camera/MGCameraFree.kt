package good.damn.engine.opengl.camera

import android.opengl.Matrix.setLookAtM
import good.damn.engine.opengl.buffers.MGBufferUniformCamera
import good.damn.engine.sdk.SDVector3
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.runnables.misc.MGRunglSendDataCameraModel
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.utils.MGUtilsBuffer
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class MGCameraFree(
    bufferUniformCamera: MGBufferUniformCamera,
    modelMatrix: MGMatrixTranslate
): MGCamera(
    bufferUniformCamera,
    modelMatrix
) {

    companion object {
        private const val MAX_PITCH = 1.57f
        private const val MIN_PITCH = -MAX_PITCH
    }

    val direction = SDVector3(
        0.0f, 0.0f, -1.0f
    )

    private val mUp = SDVector3(
        0.0f, 1.0f, 0.0f
    )

    private val mPositionDirection = SDVector3(0.0f)

    private var mSpeed = 2.0f

    private var mYaw = 0.0f
    private var mPitch = 0.0f

    private val mBufferView = MGUtilsBuffer.allocateByte(
        16 * 4
    )

    private val mRunglSendCameraModel = MGRunglSendDataCameraModel(
        mBufferView,
        bufferUniformCamera
    )

    fun invalidatePosition(
        handler: MGHandlerGl
    ) {
        val x = modelMatrix.x
        val y = modelMatrix.y
        val z = modelMatrix.z

        modelMatrix.setPosition(
            x, y, z
        )
        modelMatrix.invalidatePosition()
        setLookAtM(
            modelMatrix.model,
            0,
            x, y, z,
            direction.x + x,
            direction.y + y,
            direction.z + z,
            0.0f, 1.0f, 0.0f
        )

        mBufferView.asFloatBuffer().run {
            put(modelMatrix.model)
            position(0)
        }

        if (mRunglSendCameraModel.isUpdated) {
            mRunglSendCameraModel.isUpdated = false
            handler.post(
                mRunglSendCameraModel
            )
        }
    }

    fun addPosition(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    ) {
        mSpeed = hypot(
            x, y
        ) * 0.03f

        modelMatrix.addPosition(
            direction.x * mSpeed * -directionY,
            direction.y * mSpeed * -directionY,
            direction.z * mSpeed * -directionY
        )

        mPositionDirection.cross(
            direction,
            mUp
        )
        mPositionDirection.normalize()

        modelMatrix.addPosition(
            mPositionDirection.x * mSpeed * directionX,
            mPositionDirection.y * mSpeed * directionX,
            mPositionDirection.z * mSpeed * directionX
        )
    }

    fun addRotation(
        yaw: Float,
        pitch: Float
    ) {
        mYaw += yaw
        mPitch += pitch

        if (mPitch > MAX_PITCH)
            mPitch = MAX_PITCH

        if (mPitch < MIN_PITCH)
            mPitch = MIN_PITCH

        val cosPitch = cos(mPitch)

        direction.x = cos(mYaw) * cosPitch
        direction.y = sin(mPitch)
        direction.z = sin(mYaw) * cosPitch
        direction.normalize()
    }
}