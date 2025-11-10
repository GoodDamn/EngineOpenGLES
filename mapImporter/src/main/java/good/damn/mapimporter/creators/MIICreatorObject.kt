package good.damn.mapimporter.creators

import java.io.DataInputStream

interface MIICreatorObject<T> {
    fun create(
        stream: DataInputStream,
        optionalMask: MutableList<Byte>,
        buffer: ByteArray
    ): T
}