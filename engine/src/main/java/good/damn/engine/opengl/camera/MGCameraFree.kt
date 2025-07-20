package good.damn.engine.opengl.camera

import android.opengl.Matrix
import good.damn.engine.opengl.MGVector
import kotlin.math.cos
import kotlin.math.sin

class MGCameraFree
: MGCamera() {

    private val mDirection = MGVector(0.0f)
    private val mUp = MGVector(0.0f, 1.0f, 1.0f)
    private val mPositionDirection = MGVector(0.0f)

    private var mYaw = 0.0f
    private var mPitch = 0f

    init {
        mDirection.apply {
            x = 0f
            y = 0f
            z = -1.0f
        }
        setPosition(
            0f,
            0f,
            0f
        )
    }

    fun invalidatePosition() {
        super.invalidatePosition(
            mDirection.x + model[INDEX_X],
            mDirection.y + model[INDEX_Y],
            mDirection.z + model[INDEX_Z]
        )
    }

    fun addPosition(
        v: MGVector
    ) {
        mPositionDirection.cross(
            v,
            mUp
        )
        mPositionDirection.normalize()

        super.addPosition(
            mPositionDirection.x,
            mPositionDirection.y,
            mPositionDirection.z
        )
    }

    fun addRotation(
        yaw: Float,
        pitch: Float
    ) {
        mYaw += yaw
        mPitch += pitch

        if (mPitch > 89.0f)
            mPitch = 89.0f

        if (mPitch < -89.0f)
            mPitch = -89.0f

        val cosPitch = cos(mPitch)

        mDirection.x = cos(mYaw) * cosPitch
        mDirection.y = sin(mPitch)
        mDirection.z = sin(mYaw) * cosPitch
        mDirection.normalize()
    }

}