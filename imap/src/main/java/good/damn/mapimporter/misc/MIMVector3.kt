package good.damn.mapimporter.misc

import java.io.DataInputStream

data class MIMVector3(
    var x: Float,
    var y: Float,
    var z: Float
) {
    companion object {
        fun read(
            stream: DataInputStream
        ) = MIMVector3(
            stream.readFloat(),
            stream.readFloat(),
            stream.readFloat()
        )
    }
}