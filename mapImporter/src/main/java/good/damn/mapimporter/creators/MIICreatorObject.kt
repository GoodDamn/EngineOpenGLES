package good.damn.mapimporter.creators

import java.io.DataInputStream

interface MIICreatorObject<T> {
    fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Boolean>,
        buffer: ByteArray
    ): T
}