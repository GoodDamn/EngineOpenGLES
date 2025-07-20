package good.damn.engine.opengl

import kotlin.math.abs
import kotlin.math.sqrt

class MGVector(
    var x: Float,
    var y: Float = 0f,
    var z: Float = 0f
) {

    fun normalize() {
        val len = length()
        x /= len
        y /= len
        z /= len
    }

    fun cross(
        vect1: MGVector,
        vect2: MGVector,
    ) {
        x = vect1.y * vect2.z - vect2.y * vect1.z
        y = vect1.z * vect2.x - vect2.z * vect1.x
        z = vect1.x * vect2.y - vect2.x * vect1.y
    }

    fun cross(
        vect2: MGVector
    ) = MGVector(
        y * vect2.z - vect2.y * z,
        z * vect2.x - vect2.z * x,
        x * vect2.y - vect2.x * y
    )

    operator fun plusAssign(
        v: MGVector
    ) {
        x += v.x
        y += v.y
        z += v.z
    }

    fun length(): Float {
        return abs(sqrt(
            x * x + y * y + z * z
        ))
    }

}