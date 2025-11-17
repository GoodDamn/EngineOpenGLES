package good.damn.mapimporter.creators

import java.io.DataInputStream
import java.util.Queue

interface MIICreatorObject<T> {
    fun create(
        stream: DataInputStream,
        optionalMask: Queue<Boolean>,
        buffer: ByteArray
    ): T
}