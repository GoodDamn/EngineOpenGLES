package good.damn.ia3d.misc

import good.damn.ia3d.stream.A3DInputStream

data class A3DMVector3(
    var x: Float,
    var y: Float,
    var z: Float
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
}
