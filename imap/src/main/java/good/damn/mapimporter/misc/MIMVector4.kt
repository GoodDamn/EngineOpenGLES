package good.damn.mapimporter.misc

import java.io.DataInputStream

data class MIMVector4(
    var x: Float,
    var y: Float,
    var z: Float,
    var w: Float
) {
    companion object {
        fun read(
            stream: DataInputStream
        ) = MIMVector4(
            stream.readFloat(),
            stream.readFloat(),
            stream.readFloat(),
            stream.readFloat()
        )
    }
}