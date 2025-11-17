package good.damn.ia3d.misc

import good.damn.ia3d.stream.A3DInputStream

data class A3DMVector4(
    var x: Float,
    var y: Float,
    var z: Float,
    var w: Float
) {
    companion object {
        fun createFromStream(
            stream: A3DInputStream
        ) = A3DMVector4(
            stream.readLFloat(),
            stream.readLFloat(),
            stream.readLFloat(),
            stream.readLFloat()
        )
    }
}
