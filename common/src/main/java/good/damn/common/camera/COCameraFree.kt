package good.damn.common.camera

import android.opengl.Matrix.setLookAtM
import android.util.Log
import good.damn.common.matrices.COMatrixTranslate
import good.damn.engine.sdk.SDVector3
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class COCameraFree(
    override val modelMatrix: COMatrixTranslate
): COICameraFree {

    companion object {
        private const val MAX_PITCH = 1.55f
        private const val MIN_PITCH = -MAX_PITCH
    }

    override val direction = SDVector3(
        1.0f, 0.0f, 0.0f
    )

    private val mPositionDirection = SDVector3(0.0f)

    private val mVectorUp = SDVector3(
        0.0f, 1.0f, 0.0f
    )

    private var mSpeed = 2.0f

    private var mYaw = .0f
    private var mPitch = .0f
    private var mRoll = .0f

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
            mVectorUp.x,
            mVectorUp.y,
            mVectorUp.z
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

        modelMatrix.addPosition(
            mPositionDirection.x * mSpeed * directionX,
            mPositionDirection.y * mSpeed * directionX,
            mPositionDirection.z * mSpeed * directionX
        )
    }

    override fun addRotation(
        yaw: Float,
        pitch: Float,
        roll: Float
    ) {
        mYaw += yaw
        mPitch += pitch
        mRoll += roll

        if (mPitch > MAX_PITCH)
            mPitch = MAX_PITCH

        if (mPitch < MIN_PITCH)
            mPitch = MIN_PITCH

        val cosPitch = cos(mPitch)
        val sinPitch = sin(mPitch)
        val sinYaw = sin(mYaw)
        val cosYaw = cos(mYaw)

        direction.x = cosYaw * cosPitch // 1.0f
        direction.y = sinPitch // 0.0f
        direction.z = sinYaw * cosPitch // 0.0f
        direction.normalize()

        val dirX = direction.x
        val dirY = direction.y
        val dirZ = direction.z

        val sinRoll = sin(roll)
        val cosRoll = cos(roll)

        val negativeCosRoll = 1.0f - cosRoll

        val xs = dirX * sinRoll
        val ys = dirY * sinRoll
        val zs = dirZ * sinRoll

        val zxn = dirZ * dirX * negativeCosRoll
        val xyn = dirX * dirY * negativeCosRoll
        val yzn = dirY * dirZ * negativeCosRoll

        val x2 = mVectorUp.x
        val y2 = mVectorUp.y
        val z2 = mVectorUp.z

        mVectorUp.x = x2 * (
            dirX * dirX * negativeCosRoll + cosRoll
        ) + y2 * (
            xyn - zs
        ) + z2 * (
            zxn + ys
        )

        mVectorUp.y = x2 * (
            xyn + zs
        ) + y2 * (
            dirY * dirY * negativeCosRoll + cosRoll
        ) + z2 * (
            yzn - xs
        )

        mVectorUp.z = x2 * (
            zxn - ys
        ) + y2 * (
            yzn + xs
        ) + z2 * (
            dirZ * dirZ * negativeCosRoll + cosRoll
        )

        mVectorUp.normalize()

        /*val cosRoll = cos(mRoll)
        val sinRoll = sin(mRoll)

        mRight.x = -sinYaw // 0.0f
        mRight.y = sinRoll * cosYaw
        mRight.z = cosRoll * cosYaw
        mRight.normalize()

        mUp.cross(
            mRight,
            direction
        )
        mUp.normalize()*/

        mPositionDirection.cross(
            direction,
            mVectorUp
        )
        mPositionDirection.normalize()
    }
}