package good.damn.engine.opengl.camera

import android.util.Log
import good.damn.engine.opengl.MGVector
import kotlin.math.cos
import kotlin.math.sin

class MGCameraFree
: MGCamera() {

    companion object {
        private const val SPEED = 2.0f
    }

    private val mDirection = MGVector(
        0.0f, 0.0f, -1.0f
    )
    private val mUp = MGVector(
        0.0f, 1.0f, 0.0f
    )

    private val mPositionDirection = MGVector(0.0f)

    private var mYaw = 0.0f
    private var mPitch = 0.0f

    init {
        setPosition(
            0f,
            0f,
            3.0f
        )
        addRotation(0.0f,0.0f)
        invalidatePosition()
    }

    fun invalidatePosition() {
        Log.d("TAG", "invalidatePosition: ${mDirection.x} ${mDirection.y} ${mDirection.z};;;;$x $y $z")
        super.invalidatePosition(
            mDirection.x + x,
            mDirection.y + y,
            mDirection.z + z
        )
    }

    fun addPosition(
        x: Float,
        y: Float
    ) {
        //addPosition(0f,1f,0f)
        if (y > 0.0f) {
            addPositionNegative(mDirection)
        } else {
            addPositionPositive(mDirection)
        }

        crossDirection()
        if (x < 0.0f) {
            addPositionNegative(mPositionDirection)
        } else {
            addPositionPositive(mPositionDirection)
        }
    }

    fun addRotation(
        yaw: Float,
        pitch: Float
    ) {
        mYaw += yaw
        mPitch += pitch

        if (mPitch > 1.57f)
            mPitch = 1.57f

        if (mPitch < -1.57f)
            mPitch = -1.57f

        val cosPitch = cos(mPitch)

        mDirection.x = cos(mYaw) * cosPitch
        mDirection.y = sin(mPitch)
        mDirection.z = sin(mYaw) * cosPitch
        mDirection.normalize()
    }

    private inline fun crossDirection() {
        mPositionDirection.cross(
            mDirection,
            mUp
        )
        mPositionDirection.normalize()
    }

    private inline fun addPositionPositive(
        v: MGVector
    ) {
        super.addPosition(
            v.x * SPEED,
            v.y * SPEED,
            v.z * SPEED
        )
    }

    private inline fun addPositionNegative(
        v: MGVector
    ) {
        super.addPosition(
            v.x * -SPEED,
            v.y * -SPEED,
            v.z * -SPEED
        )
    }
}