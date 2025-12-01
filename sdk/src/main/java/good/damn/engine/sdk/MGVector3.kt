package good.damn.engine.sdk

import kotlin.math.abs
import kotlin.math.sqrt

class MGVector3(
    var x: Float,
    var y: Float = 0f,
    var z: Float = 0f
) {

    fun copy(
        v: MGVector3
    ) {
        x = v.x
        y = v.y
        z = v.z
    }

    fun normalize() {
        val len = length()
        x /= len
        y /= len
        z /= len
    }

    fun interpolate(
        v: MGVector3,
        f: Float
    ): MGVector3 {
        val result = MGVector3(0f)
        result.x = (x + v.x) * f
        result.y = (y + v.y) * f
        result.z = (z + v.z) * f
        return result
    }

    fun cross(
        vect1: MGVector3,
        vect2: MGVector3,
    ) {
        x = vect1.y * vect2.z - vect2.y * vect1.z
        y = vect1.z * vect2.x - vect2.z * vect1.x
        z = vect1.x * vect2.y - vect2.x * vect1.y
    }

    fun cross(
        vect2: MGVector3
    ) = MGVector3(
        y * vect2.z - vect2.y * z,
        z * vect2.x - vect2.z * x,
        x * vect2.y - vect2.x * y
    )

    operator fun minusAssign(
        v: MGVector3
    ) {
        x -= v.x
        y -= v.y
        z -= v.z
    }

    operator fun plusAssign(
        v: MGVector3
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

    override fun toString() = "X=$x Y=$y Z=$z"

}