package good.damn.opengles_engine.opengl.camera

import android.opengl.Matrix
import android.util.Log
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.sin

class RotationCamera
: BaseCamera() {
    companion object{
        private const val TAG = "RotationCamera"
    }

    var radius: Float = 6f
        set(v) {
            setRotation(mHDegrees, mVDegrees)
            field = v
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