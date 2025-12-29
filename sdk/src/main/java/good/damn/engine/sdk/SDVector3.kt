package good.damn.engine.sdk

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class SDVector3(
    var x: Float,
    var y: Float = 0f,
    var z: Float = 0f
) {

    fun copy(
        v: SDVector3
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
        v: SDVector3,
        f: Float
    ): SDVector3 {
        val result = SDVector3(0f)
        result.x = (x + v.x) * f
        result.y = (y + v.y) * f
        result.z = (z + v.z) * f
        return result
    }

    fun cross(
        vect1: SDVector3,
        vect2: SDVector3,
    ) {
        x = vect1.y * vect2.z - vect2.y * vect1.z
        y = vect1.z * vect2.x - vect2.z * vect1.x
        z = vect1.x * vect2.y - vect2.x * vect1.y
    }

    fun cross(
        vect2: SDVector3
    ) = SDVector3(
        y * vect2.z - vect2.y * z,
        z * vect2.x - vect2.z * x,
        x * vect2.y - vect2.x * y
    )

    fun maxValue() = max(
        x, max(y, z)
    )

    operator fun minusAssign(
        v: SDVector3
    ) {
        x -= v.x
        y -= v.y
        z -= v.z
    }

    operator fun plusAssign(
        v: SDVector3
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