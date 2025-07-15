package good.damn.engine.opengl.camera

import android.opengl.Matrix
import kotlin.math.cos
import kotlin.math.sin

class MGCameraRotation
: MGCamera() {
    companion object{
        private const val TAG = "RotationCamera"
    }

    var radius: Float = 6f
        set(v) {
            if (v < 0.0f) {
                return
            }
            field = v
            setRotation(mHDegrees, mVDegrees)
        }

    private var mHDegrees: Float = 1f
    private var mVDegrees: Float = 1f

    fun rotateBy(
        x: Float,
        y: Float
    ) {
        setRotation(
            mHDegrees + x,
            mVDegrees + y
        )
    }

    fun setRotation(
        x: Float,
        y: Float
    ) {

        if (y < 0.0f || y > 3.1415f) {
            return
        }

        mHDegrees = x
        mVDegrees = y
        
        val ysin = sin(mVDegrees)
        
        setPosition(
            radius * cos(mHDegrees) * ysin,
            radius * cos(mVDegrees),
            radius * ysin * sin(mHDegrees)
        )

        Matrix.setLookAtM(
            model,
            0,
            model[12], model[13], model[14], // Position
            0f, 0f, 0f, // AT
            0f, 1f, 0f
        )
    }

}