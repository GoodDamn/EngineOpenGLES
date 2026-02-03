package good.damn.common.camera

import android.opengl.Matrix
import android.opengl.Matrix.setLookAtM
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

    private val mRotationYaw = FloatArray(16)
    private val mRotationPitch = FloatArray(16)
    //private val mRotationRoll = FloatArray(16)

    private val mRotationResult = FloatArray(16)

    private val mVector = FloatArray(4)

    private var mSpeed = 2.0f

    private var mYaw = .0f
    private var mPitch = .0f

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

    private var mRoll = 0f

    override fun addRotation(
        yaw: Float,
        pitch: Float,
        roll: Float
    ) {
        mRoll += roll
        mYaw += yaw
        mPitch += pitch


        /*Matrix.setRotateM(
            mRotationYaw,
            0,
            Math.toDegrees(
                -yaw.toDouble()
            ).toFloat(),
            0.0f,
            1.0f,
            0.0f
        )

        Matrix.setRotateM(
            mRotationPitch,
            0,
            Math.toDegrees(
                pitch.toDouble()
            ).toFloat(),
            0.0f,
            0.0f,
            1.0f
        )

        Matrix.multiplyMM(
            mRotationResult,
            0,
            mRotationPitch,
            0,
            mRotationYaw,
            0
        )

        copyToVector(
            direction
        )

        copyToVector(
            mVectorUp
        )*/

/*if (mPitch > MAX_PITCH)
            mPitch = MAX_PITCH

        if (mPitch < MIN_PITCH)
            mPitch = MIN_PITCH*/

        val cosPitch = cos(mPitch)
        val sinPitch = sin(mPitch)
        val sinYaw = sin(mYaw)
        val cosYaw = cos(mYaw)

        direction.x = cosYaw * cosPitch // 1.0f
        direction.y = sinPitch // 0.0f
        direction.z = sinYaw * cosPitch // 0.0f
        direction.normalize()

        /*Matrix.setRotateM(
            mRotationResult,
            0,
            Math.toDegrees(
                roll.toDouble()
            ).toFloat(),
            direction.x,
            direction.y,
            direction.z
        )

        copyToVector(
            mVectorUp
        )*/

        /*rotateVector(
            roll,
            direction,
            mVectorUp
        )

        val cosRoll = cos(mRoll)
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

    private inline fun copyToVector(
        to: SDVector3
    ) {
        mVector[0] = to.x
        mVector[1] = to.y
        mVector[2] = to.z

        Matrix.multiplyMV(
            mVector,
            0,
            mRotationResult,
            0,
            mVector,
            0
        )

        to.x = mVector[0]
        to.y = mVector[1]
        to.z = mVector[2]
        to.normalize()
    }

    private inline fun rotateVector(
        radians: Float,
        inputVector: SDVector3,
        outputVector: SDVector3
    ) {
        val dirX = inputVector.x
        val dirY = inputVector.y
        val dirZ = inputVector.z

        val sinRoll = sin(radians)
        val cosRoll = cos(radians)

        val negativeCosRoll = 1.0f - cosRoll

        val xs = dirX * sinRoll
        val ys = dirY * sinRoll
        val zs = dirZ * sinRoll

        val zxn = dirZ * dirX * negativeCosRoll
        val xyn = dirX * dirY * negativeCosRoll
        val yzn = dirY * dirZ * negativeCosRoll

        val x2 = outputVector.x
        val y2 = outputVector.y
        val z2 = outputVector.z

        outputVector.x = x2 * (
            dirX * dirX * negativeCosRoll + cosRoll
        ) + y2 * (
            xyn - zs
        ) + z2 * (
            zxn + ys
        )

        outputVector.y = x2 * (
            xyn + zs
        ) + y2 * (
            dirY * dirY * negativeCosRoll + cosRoll
        ) + z2 * (
            yzn - xs
        )

        outputVector.z = x2 * (
            zxn - ys
        ) + y2 * (
            yzn + xs
        ) + z2 * (
            dirZ * dirZ * negativeCosRoll + cosRoll
        )

        outputVector.normalize()
    }
}