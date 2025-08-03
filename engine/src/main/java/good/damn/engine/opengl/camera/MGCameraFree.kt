package good.damn.engine.opengl.camera

import good.damn.engine.opengl.MGVector
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class MGCameraFree
: MGCamera() {

    val direction = MGVector(
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
        super.invalidatePosition(
            direction.x + x,
            direction.y + y,
            direction.z + z
        )
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

        super.addPosition(
            direction.x * mSpeed * -directionY,
            direction.y * mSpeed * -directionY,
            direction.z * mSpeed * -directionY
        )

        mPositionDirection.cross(
            direction,
            mUp
        )
        mPositionDirection.normalize()

        super.addPosition(
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

        if (mPitch > 1.56f)
            mPitch = 1.56f

        if (mPitch < -1.56f)
            mPitch = -1.56f

        val cosPitch = cos(mPitch)

        direction.x = cos(mYaw) * cosPitch
        direction.y = sin(mPitch)
        direction.z = sin(mYaw) * cosPitch
        direction.normalize()
    }
}