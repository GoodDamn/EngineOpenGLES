package good.damn.engine.opengl.camera

import android.util.Log
import good.damn.engine.opengl.MGVector
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class MGCameraFree
: MGCamera() {

    private val mDirection = MGVector(
        0.0f, 0.0f, -1.0f
    )
    private val mUp = MGVector(
        0.0f, 1.0f, 0.0f
    )

    private val mPositionDirection = MGVector(0.0f)

    private var mSpeed = 2.0f

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
        y: Float,
        directionX: Float,
        directionY: Float
    ) {
        Log.d("MGCameraFree", "addPosition: $x::$y")
        mSpeed = hypot(
            x, y
        ) * 0.03f

        super.addPosition(
            mDirection.x * mSpeed * -directionY,
            mDirection.y * mSpeed * -directionY,
            mDirection.z * mSpeed * -directionY
        )

        /*if (y > 0.0f) {
            addPositionNegative(mDirection)
        } else {
            addPositionPositive(mDirection)
        }*/

        crossDirection()

        super.addPosition(
            mPositionDirection.x * mSpeed * directionX,
            mPositionDirection.y * mSpeed * directionX,
            mPositionDirection.z * mSpeed * directionX
        )

        /*if (x < 0.0f) {
            addPositionNegative(mPositionDirection)
        } else {
            addPositionPositive(mPositionDirection)
        }*/
    }

    fun addRotation(
        yaw: Float,
        pitch: Float
    ) {
        mYaw += yaw
        mPitch += pitch

        if (mPitch > 1.56f)
            mPitch = 1.56f

        if (mPitch < -1.56f)
            mPitch = -1.56f

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
            v.x * mSpeed,
            v.y * mSpeed,
            v.z * mSpeed
        )
    }

    private inline fun addPositionNegative(
        v: MGVector
    ) {
        super.addPosition(
            v.x * -mSpeed,
            v.y * -mSpeed,
            v.z * -mSpeed
        )
    }
}