package good.damn.ia3d.misc

import good.damn.ia3d.stream.A3DInputStream

data class A3DMVector3(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f
) {
    companion object {
        fun createFromStream(
            stream: A3DInputStream
        ) = A3DMVector3(
            stream.readLFloat(),
            stream.readLFloat(),
            stream.readLFloat()
        )
    }

    fun subtract(
        v: A3DMVector3,
        vv: A3DMVector3
    ) {
        x = v.x - vv.x
        y = v.y - vv.y
        z = v.z - vv.z
    }
}
