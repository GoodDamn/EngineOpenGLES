package good.damn.engine.opengl.camera

import android.opengl.Matrix.setLookAtM
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.matrices.MGMMatrix
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class MGCameraFree(
    modelMatrix: MGMatrixTranslate
): MGCamera(
    modelMatrix
) {

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
        addRotation(0.0f,0.0f)
        invalidatePosition()
    }

    fun invalidatePosition() {
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