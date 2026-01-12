package good.damn.common.camera

import android.opengl.Matrix.setLookAtM
import good.damn.common.matrices.MGMatrixTranslate
import good.damn.engine.sdk.SDVector3
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class COCameraFree(
    val modelMatrix: MGMatrixTranslate
): COICameraFree {

    companion object {
        private const val MAX_PITCH = 1.55f
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

    override fun invalidatePosition() {
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
    }

    override fun addPosition(
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

    override fun addRotation(
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