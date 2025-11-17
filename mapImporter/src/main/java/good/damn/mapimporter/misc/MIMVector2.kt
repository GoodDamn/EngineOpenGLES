package good.damn.mapimporter.misc

import java.io.DataInputStream

data class MIMVector2(
    var x: Float,
    var y: Float
) {
    companion object {
        fun read(
            stream: DataInputStream
        ) = MIMVector2(
            stream.readFloat(),
            stream.readFloat()
        )
    }
}